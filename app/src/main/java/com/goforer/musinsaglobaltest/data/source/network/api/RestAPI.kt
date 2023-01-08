package com.goforer.musinsaglobaltest.data.source.network.api

import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse
import com.goforer.musinsaglobaltest.data.source.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface RestAPI {
    @GET("/interview/list.json")
    fun getGoodsList(): Flow<ApiResponse<GoodsListResponse>>
}