package com.goforer.musinsaglobaltest.data.source.model.entity.local.state.home

import android.os.Parcelable
import com.goforer.musinsaglobaltest.data.source.model.entity.home.BaseEntity
import com.goforer.musinsaglobaltest.presentation.stateholder.MediatorStatedViewModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ContentState(
    val viewModel: @RawValue MediatorStatedViewModel,
    val firstVisiblePosition: Int,
    val lastVisibleItemPos: Int
) : BaseEntity(), Parcelable