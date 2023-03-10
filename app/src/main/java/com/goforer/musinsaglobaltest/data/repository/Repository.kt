package com.goforer.musinsaglobaltest.data.repository

import com.goforer.musinsaglobaltest.data.Query
import com.goforer.musinsaglobaltest.data.source.network.NetworkErrorHandler
import com.goforer.musinsaglobaltest.data.source.network.api.RestAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class Repository<Resource> {
    @Inject
    lateinit var restAPI: RestAPI

    @Inject
    lateinit var networkErrorHandler: NetworkErrorHandler

    abstract fun handle(viewModelScope: CoroutineScope, replyCount: Int, query: Query): Flow<Resource>

    open fun invalidatePagingSource() {}

    protected fun handleNetworkError(errorMessage: String) {
        networkErrorHandler.handleError(errorMessage)
    }
}