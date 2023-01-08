package com.goforer.musinsaglobaltest.domain

import com.goforer.musinsaglobaltest.data.Params
import com.goforer.musinsaglobaltest.data.repository.Repository
import com.goforer.musinsaglobaltest.data.source.network.response.Resource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Singleton
abstract class RepoUseCase(open val repository: Repository<Resource>) : UseCase<Resource>() {
    override fun run(viewModelScope: CoroutineScope, params: Params) =
        repository.handle(viewModelScope, params.query)

    fun invalidatePagingSource() = repository.invalidatePagingSource()
}