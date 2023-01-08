package com.goforer.musinsaglobaltest.presentation.event.home

import com.goforer.musinsaglobaltest.data.source.model.entity.local.state.home.ContentState
import com.goforer.musinsaglobaltest.presentation.event.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentsEventBus
@Inject
constructor() : EventBus<ContentState?>()