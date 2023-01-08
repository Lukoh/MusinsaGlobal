package com.goforer.musinsaglobaltest.presentation.stateholder.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goforer.musinsaglobaltest.data.Params
import com.goforer.musinsaglobaltest.domain.home.GetGoodsListUseCase
import com.goforer.musinsaglobaltest.presentation.stateholder.MediatorStatedViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class GetGoodsListViewModel
@AssistedInject
constructor(
    useCase: GetGoodsListUseCase,
    @Assisted private val params: Params
) : MediatorStatedViewModel(useCase, params) {
    @AssistedFactory
    interface AssistedVMFactory {
        fun create(params: Params): GetGoodsListViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: AssistedVMFactory, params: Params) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(params) as T
                }
            }
    }
}