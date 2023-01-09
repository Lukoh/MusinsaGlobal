package com.goforer.musinsaglobaltest.data.repository.home

import com.goforer.musinsaglobaltest.data.Query
import com.goforer.musinsaglobaltest.data.repository.Repository
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse
import com.goforer.musinsaglobaltest.data.source.network.mediator.DataMediator
import com.goforer.musinsaglobaltest.data.source.network.response.Resource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGoodsListRepository
@Inject
constructor() : Repository<Resource>() {
    override fun handle(viewModelScope: CoroutineScope, query: Query) = object :
        DataMediator<GoodsListResponse>(viewModelScope) {
        override fun load() = restAPI.getGoodsList()
    }.asSharedFlow
}