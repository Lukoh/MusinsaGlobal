package com.goforer.musinsaglobaltest.domain

import com.goforer.musinsaglobaltest.data.Params
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Singleton

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 */

@Singleton
abstract class UseCase<Value> {
    abstract fun run(viewModelScope: CoroutineScope, params: Params): Flow<Value>
}