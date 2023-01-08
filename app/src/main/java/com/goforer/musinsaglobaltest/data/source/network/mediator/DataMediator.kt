package com.goforer.musinsaglobaltest.data.source.network.mediator

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.goforer.musinsaglobaltest.data.repository.Repository.Companion.ITEM_COUNT
import com.goforer.musinsaglobaltest.data.source.network.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber

/**
 * A generic class that can provide a resource backed by the network.
 * <p>
 * You can read more about it in the <a href="https://developer.android.com/arch">Architecture
 * Guide</a>.
 */
abstract class DataMediator<Response> constructor(
    viewModelScope: CoroutineScope, private val enabledCache: Boolean
) {
    private val resource by lazy {
        Resource()
    }

    internal val asSharedFlow = flow {
        emit(resource.loading(Status.LOADING))
        clearCache()
        load().collect { apiResponse ->
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    if (enabledCache) {
                        saveToCache(apiResponse.body)
                        loadFromCache(false, ITEM_COUNT, 1)
                    } else {
                        emit(resource.success(apiResponse.body))
                    }
                }

                is ApiEmptyResponse -> {
                    emit(resource.success(""))
                }

                is ApiErrorResponse -> {
                    Timber.e("Network-Error: ${apiResponse.errorMessage}")
                    emit(resource.error(apiResponse.errorMessage, apiResponse.statusCode))
                    onNetworkError(apiResponse.errorMessage, apiResponse.statusCode)
                }
            }
        }
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 10
    )

    protected open suspend fun onNetworkError(errorMessage: String, errorCode: Int) {
    }

    @WorkerThread
    protected open suspend fun saveToCache(value: Response) {
    }

    @MainThread
    protected open fun loadFromCache(
        isLatest: Boolean,
        itemCount: Int,
        pages: Int
    ): Flow<Response>? = null

    @MainThread
    protected abstract fun load(): Flow<ApiResponse<Response>>

    protected open suspend fun clearCache() {}
}