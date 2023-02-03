package com.goforer.musinsaglobaltest.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.goforer.base.extension.*
import com.goforer.musinsaGlobalTest.databinding.FragmentHomeBinding
import com.goforer.musinsaglobaltest.data.Params
import com.goforer.musinsaglobaltest.data.Query
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse
import com.goforer.musinsaglobaltest.data.source.model.entity.local.state.home.ContentState
import com.goforer.musinsaglobaltest.data.source.network.NetworkError.Companion.ERROR_SERVICE_BAD_GATEWAY
import com.goforer.musinsaglobaltest.data.source.network.NetworkError.Companion.ERROR_SERVICE_UNAVAILABLE
import com.goforer.musinsaglobaltest.data.source.network.response.Status
import com.goforer.musinsaglobaltest.presentation.event.home.ContentsEventBus
import com.goforer.musinsaglobaltest.presentation.event.home.style.StylesEventBus
import com.goforer.musinsaglobaltest.presentation.stateholder.MediatorStatedViewModel
import com.goforer.musinsaglobaltest.presentation.stateholder.home.GetGoodsListViewModel
import com.goforer.musinsaglobaltest.presentation.ui.BaseFragment
import com.goforer.musinsaglobaltest.presentation.ui.home.adapter.DataAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    private lateinit var dataAdapter: DataAdapter

    private var linearLayoutManager: LinearLayoutManager? = null

    private var contentState: ContentState? = null

    private var getGoodsListViewModel: MediatorStatedViewModel? = null

    private var lastPosition = 0

    @Inject
    internal lateinit var getGoodsListViewModelFactory: GetGoodsListViewModel.AssistedVMFactory

    @Inject
    internal lateinit var stylesEventBus: StylesEventBus

    @Inject
    internal lateinit var contentsEventBus: ContentsEventBus

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이 Fragment 성성 시 Fragment 가 종료되기 포스팅 된 Content 정보들을 Observing 해서 포스팅 된
        // Content 정보들이 있으면 로딩한다.
        observeReloadContents()
        load()
    }

    override fun onDestroyView() {
        binding.rvGoods.adapter = null

        super.onDestroyView()
    }

    override fun onStop() {
        /**
         * Jetpack 라이브러리인 Navigation 에서 현재 Fragment 에서 다른 Fragment 를 호출 할시 현재 Fragment 는
         * Destroyed 됩니다. 이때 현재 Content 정보들을 Event Bus 를 통해서 저장해야 호출된 Fragment 가 종료 되고
         * 현재 Fragment 가 다시 보여질때 저장된 Content 정보들을 가지고 새로 보여지게 해야 합니다.
         *
         */
        linearLayoutManager?.let {
            saveContentsState(
                it.findFirstVisibleItemPosition(),
                it.findLastVisibleItemPosition(),
            )
        }

        super.onStop()
    }

    /**
     * CurrentState 를 통해서 HomeFragment 가 content 정보들을 새로 로딩 하는것인지 아니면 Reloaded 되는것인지 체크 하여
     * 현재 content 정보들 보여지게 합니다. content 정보들을 Reloaded 되는 것이면 새로 네트워크 리소스를 사용하여 백엔드 서버로
     * 부터 다시 content 정보들을 가져 오는 것이 아니라 캐쉬되어 있는 Content 정보들을 가지고 와서 보여지게 합니다.
     *
     */
    private fun load() {
        view?.let {
            setLoading(true)
            setDataAdapter()
            contentState.isNull({
                submitDataList(createViewModel(), false)
            }, {
                reloadContents(it)
            })
        }
    }

    private fun observeReloadContents() {
        contentsEventBus.subscribe(mainActivity.lifecycle) {
            contentState = it
        }
    }

    private fun setDataAdapter() {
        dataAdapter = DataAdapter(mainActivity, {
                lifecycleScope.launch {
                    loadContent(it)
                }
            }, {
                if (isAdded && isNavControllerInitialized()) {
                    val direction = HomeFragmentDirections.actionHomeFragmentToStyleListFragment()

                    stylesEventBus.post(mainActivity.lifecycle, it)
                    navController.safeNavigate(direction.actionId, direction)
                }
            }
        )

        if (::dataAdapter.isInitialized)
            setRecyclerView(binding.rvGoods, dataAdapter)
    }

    private fun setRecyclerView(rvGoods: RecyclerView, goodsAdapter: DataAdapter) {
        rvGoods.apply {
            linearLayoutManager =
                LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false)

            linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
            goodsAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            itemAnimator?.changeDuration = 0
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            setHasFixedSize(false)
            setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
            layoutManager = linearLayoutManager
            adapter = goodsAdapter
        }
    }

    /**
     * 현재 Content 정보들을 Event Bus 를 통해서 저장해야 호출된 Fragment 가 종료 되고
     * 현재 Fragment 가 다시 보여질때 저장된 Content 정보들을 가지고 새로 보여지게 해야 합니다.
     *
     * 이 부분은 현재 Content 정보들을 CurrentState 데이터 클래슬 생성해서 현재 Content 정보들을 넣고
     * Event Bus 를 통해서 포스팅 합니다.
     * (정확하게는 사용된 ViewModel 을 포스팅 하는것입니다. 이 ViewModel 내 value property 에서
     * 현재 Content 정보들을 뽑아 올수 있습니다. 이유는 이 ViewModel 내에서 사용된 Flow 가 Hot Flow 인
     * StateFlow 이기 때문입니다. 이 루틴을 통해서 포스팅 되고 다시 이 Fragment 로 돌아 왔을때 포스팅된 것을
     * Observing 합니다.)
     */
    private fun saveContentsState(firstPosition: Int, lastPosition: Int) {
        getGoodsListViewModel?.let {
            createContentsState(it, firstPosition, lastPosition)
            contentState?.let { state ->
                contentsEventBus.post(mainActivity.lifecycle, state)
            }
        }
    }

    /**
     * 현재 Content 정보들을 Event Bus 를 통해서 포스팅 하기 위해서 ContentState 인스턴스를 생성합니다.
     *
     */
    private fun createContentsState(
        viewModel: MediatorStatedViewModel,
        firstPosition: Int,
        lastPosition: Int
    ) {
        contentState = ContentState(viewModel, firstPosition, lastPosition)
    }

    /**
     * 벡엔드서버와 통신을 통해서 Content 정보들을 가여오기 위한 ViewModel 을 생성하고 요청을 시작합니다.
     *
     */
    private fun createViewModel() : MediatorStatedViewModel {
        val viewModel = GetGoodsListViewModel.provideFactory(
            getGoodsListViewModelFactory, Params(1, Query())
        ).create(GetGoodsListViewModel::class.java)

        getGoodsListViewModel = viewModel

        return viewModel
    }

    /**
     * 벡엔드서버와 통신을 통해서 받아온 Content 정보들을 보여줍니다.
     *
     */
    private fun submitDataList(viewModel: MediatorStatedViewModel, isReloaded: Boolean) {
        view?.let {
            launchAndRepeatWithViewLifecycle {
                viewModel.value.collectLatest { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let {
                                @Suppress("UNCHECKED_CAST")
                                val result = resource.data as? GoodsListResponse

                                result?.let {
                                    setLoading(false)
                                    lifecycleScope.launch {
                                        binding.rvGoods.show()
                                    }

                                    if (::dataAdapter.isInitialized)
                                        dataAdapter.submitList(it.data)
                                }
                            }
                        }

                        Status.ERROR -> {
                            setLoading(false)
                            if (resource.errorCode == ERROR_SERVICE_UNAVAILABLE
                                    || resource.errorCode == ERROR_SERVICE_BAD_GATEWAY)
                                handleNotAvailableNetwork()
                            else
                                showErrorPopup(resource.message!!) {}

                        }

                        Status.LOADING -> {
                            if (!isReloaded)
                                setLoading(true)
                        }
                    }
                }
            }
        }
    }

    /**
     * Event Bus 를 통해서 포스팅된 이전의 Content 정보들을 다시 로딩헤서 보여줍니다.
     * 이렇해 하면 비용이 많이 드는 네트워크 리소소를 사용하지 않아서 훨씬 효율적입니다.
     *
     *  Content 정보들은 Event Bus 를 통해서 포스팅 된 현재 정보들을 담고 있는 ViewModel 내의 Hot Flow 인 StateFlow 을 사용해서 저장됩니다.
     *  StateFlow 는 마지막으로 내보낸 항목을 캐시 하고 있기 때문입니다,
     *
     *  PS : 버퍼링이 필요할 경우는 Shared Flow 를 사용하면 됩니다.
     *
     *  repeatOnLifecycle API 적용 - 백그라운드 상태일 경우 다시 콘텐츠들을 로드 할 필요가 없고, 리소스 낭비 및
     *  앱 Crash 에방을 위해서 적용
     *  적용이유:
     *  The Lifecycle.repeatOnLifecycle API was primarily born to allow safer Flow collection from
     *  the UI layer in Android. Its restartable behavior, that takes into consideration
     *  the UI lifecycle, makes it the perfect default API to process items only when the UI is
     *  visible on the screen.
     */
    private fun reloadContents(contentState: ContentState) {
        view?.let {
            launchAndRepeatWithViewLifecycle {
                getGoodsListViewModel = contentState.viewModel as GetGoodsListViewModel
                lastPosition = contentState.lastVisibleItemPos
                setLoading(enabled = false)
                getGoodsListViewModel?.let {
                    submitDataList(it, true)
                }
            }
        }
    }

    /**
     * Shimmer 라이브러리를 이용하여 리스트 로딩을 보여줍니다.
     */
    private fun setLoading(enabled: Boolean) {
        lifecycleScope.launch {
            with(binding) {
                if (enabled) {
                    shimmerViewContainer.show()
                    shimmerViewContainer.startShimmer()
                } else {
                    shimmerViewContainer.gone()
                    shimmerViewContainer.stopShimmer()
                }
            }
        }
    }

    private fun handleNotAvailableNetwork() {
        if (isAdded && isNavControllerInitialized()) {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToNetworkDisconnectFragment()

            navController.safeNavigate(direction.actionId, direction)
        }
    }
}