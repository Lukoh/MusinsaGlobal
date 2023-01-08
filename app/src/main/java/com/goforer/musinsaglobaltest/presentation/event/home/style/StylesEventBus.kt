package com.goforer.musinsaglobaltest.presentation.event.home.style

import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Style
import com.goforer.musinsaglobaltest.presentation.event.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StylesEventBus
@Inject
constructor() : EventBus<List<Style>>()