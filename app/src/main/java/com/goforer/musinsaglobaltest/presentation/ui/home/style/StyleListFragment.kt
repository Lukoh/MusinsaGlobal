package com.goforer.musinsaglobaltest.presentation.ui.home.style

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.recyclerview.widget.SimpleItemAnimator
import com.goforer.musinsaGlobalTest.databinding.FragmentStyleListBinding
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Style
import com.goforer.musinsaglobaltest.presentation.event.home.style.StylesEventBus
import com.goforer.musinsaglobaltest.presentation.ui.BaseFragment
import com.goforer.musinsaglobaltest.presentation.ui.MainActivity
import com.goforer.musinsaglobaltest.presentation.ui.home.style.adapter.StyleAdapter
import javax.inject.Inject

class StyleListFragment : BaseFragment<FragmentStyleListBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStyleListBinding
        get() = FragmentStyleListBinding::inflate

    private lateinit var styleAdapter: StyleAdapter

    @Inject
    internal lateinit var stylesEventBus: StylesEventBus

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeStyles()
        setOnClickListener()
    }

    override fun onDestroyView() {
        binding.rvStyles.adapter = null

        super.onDestroyView()
    }

    private fun observeStyles() {
        stylesEventBus.subscribe(mainActivity.lifecycle, true) { styles ->
            showStyles(styles)
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            arrowContainer.setOnClickListener {
                handleBackPressed()
                if (NavHostFragment.findNavController(this@StyleListFragment)
                        .popBackStack()
                        .not()
                ) {
                    activity?.finishAndRemoveTask()
                }
            }
        }
    }

    private fun setAdapter() {
        styleAdapter = StyleAdapter(mainActivity) {
            loadContent(it)
        }

        if (::styleAdapter.isInitialized)
            setRecyclerView(mainActivity, styleAdapter)
    }

    private fun setRecyclerView(context: Context, styleAdapter: StyleAdapter) {
        with(binding) {
            rvStyles.apply {
                val linearLayoutManager =
                    LinearLayoutManager(context as MainActivity, RecyclerView.VERTICAL, false)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                styleAdapter.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
                itemAnimator?.changeDuration = 0
                (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                setHasFixedSize(true)
                layoutManager = linearLayoutManager
                adapter = styleAdapter
            }
        }
    }

    private fun showStyles(styles: List<Style>) {
        styleAdapter.submitList(styles)
    }
}