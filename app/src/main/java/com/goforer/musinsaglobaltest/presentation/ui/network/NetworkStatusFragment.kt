package com.goforer.musinsaglobaltest.presentation.ui.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.goforer.base.extension.safeNavigate
import com.goforer.base.utils.connect.ConnectionUtils
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaGlobalTest.databinding.FragmentNetworkNotAvailableBinding
import com.goforer.musinsaglobaltest.presentation.ui.BaseFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NetworkStatusFragment : BaseFragment<FragmentNetworkNotAvailableBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNetworkNotAvailableBinding
        get() = FragmentNetworkNotAvailableBinding::inflate

    private val timer = Timer()
    private var loadTimerTask: TimerTask? = null

    companion object {
        private const val PAGE_DURATION = 2000L
    }

    override fun onResume() {
        super.onResume()

        resumeTimerTask()
    }

    override fun onPause() {
        pauseTimerTask()

        super.onPause()
    }

    override fun onDestroy() {
        loadTimerTask?.let {
            pauseTimerTask()
        }

        timer.cancel()

        super.onDestroy()
    }

    private fun loadTimerTaskMaker() = object : TimerTask() {
        override fun run() {
            lifecycleScope.launch {
                withContext(coroutineContext) {
                    if (ConnectionUtils.isNetworkAvailable(context)) {
                        if (isAdded)
                            findNavController().safeNavigate(R.id.nav_graph, R.id.nav_graph)
                    }
                }
            }
        }
    }

    private fun pauseTimerTask() {
        loadTimerTask?.cancel()
        loadTimerTask = null
        timer.purge()
    }

    private fun resumeTimerTask() {
        loadTimerTask = loadTimerTaskMaker()
        timer.scheduleAtFixedRate(loadTimerTask, PAGE_DURATION, PAGE_DURATION)
    }
}