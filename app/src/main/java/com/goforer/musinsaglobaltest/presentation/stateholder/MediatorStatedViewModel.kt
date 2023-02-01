package com.goforer.musinsaglobaltest.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goforer.musinsaglobaltest.data.Params
import com.goforer.musinsaglobaltest.data.source.network.response.Resource
import com.goforer.musinsaglobaltest.data.source.network.response.Status
import com.goforer.musinsaglobaltest.domain.RepoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

open class MediatorStatedViewModel(private val useCase: RepoUseCase, params: Params) : ViewModel() {
    // You can implement code blow:
    // Just please visit below link if you'd like to know [StatFlow & SharedFlow]
    // Link : https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
    /*
    val value = useCase.run(viewModelScope, params).flatMapLatest {
        flow {
            emit(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Resource().loading(LOADING)
    )

     */

    fun invalidatePagingSource() = useCase.invalidatePagingSource()

    /**
     * Hot Flow 인 StateFlow 는 초기 값이 필요하며 Collector 가 수집을 시작하는 즉시 내보냅니다.
     * 이 StateFLow 를 이용하면 데이터를 항상 볼 수 있도록 마지막으로 내보낸 데이타들을 캐싱 할 수 있습니다.
     * 즉, StateFlow 는 마지막 데이타들을 저장하고 Collector 가 수집을 시작하자마자 이를 내보냅니다.
     */
    private val _value = MutableStateFlow(Resource().loading(Status.LOADING))
    val value = _value

    init {
        viewModelScope.launch {
            useCase.run(viewModelScope, params).distinctUntilChanged { old, new ->
                old != new
            } .collectLatest {
                _value.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        value.value = Resource().loading(Status.LOADING)
    }
}