package com.goforer.musinsaglobaltest.presentation.ui

import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.goforer.base.utils.connect.ConnectionUtils
import com.goforer.base.view.dialog.NormalDialog
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaglobaltest.di.Injectable

abstract class BaseFragment<T : ViewBinding> : Fragment(), Injectable {
    private var _binding: T? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    internal val binding
        get() = _binding as T

    internal var isLoading = false

    internal lateinit var mainActivity: MainActivity

    private lateinit var context: Context

    private var errorDialogMsg = ""

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var isFromBackStack = mutableMapOf<String, Boolean>()

    private var customTabsServiceConnection: CustomTabsServiceConnection? = null

    protected open lateinit var navController: NavController

    protected var client: CustomTabsClient? = null
    protected var customTabsSession: CustomTabsSession? = null

    open fun needTransparentToolbar() = false
    open fun needSystemBarTextWhite() = false

    companion object {
        const val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"

        const val RECYCLER_VIEW_CACHE_SIZE = 21
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createCustomTabsServiceConnection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = _binding ?: bindingInflater.invoke(inflater, container, false)
        (activity as MainActivity).supportActionBar?.hide()
        navController =
            (mainActivity.supportFragmentManager.fragments.first() as NavHostFragment).navController

        return requireNotNull(_binding).root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        customTabsServiceConnection?.let {
            mainActivity.unbindService(customTabsServiceConnection!!)
            customTabsServiceConnection = null
            client = null
            customTabsSession = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context = context
        mainActivity = (context as MainActivity?)!!
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()

        if (::onBackPressedCallback.isInitialized) {
            onBackPressedCallback.isEnabled = false
            onBackPressedCallback.remove()
        }
    }

    override fun getContext() = context

    open suspend fun doOnFromBackground() {
    }

    protected open fun handleBackPressed() {
    }

    protected fun isNavControllerInitialized() = ::navController.isInitialized

    private fun showDefaultDialog(
        message: CharSequence, title: CharSequence? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        hideKeyboard()

        val builder = NormalDialog.Builder()

        builder.setContext(mainActivity)
        builder.setHorizontalMode(true)
        title?.let { builder.setTitle(title) }
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok) { _, _ ->
        }

        builder.setOnDismissListener(
            DialogInterface.OnDismissListener {
                onDismiss?.let { it1 -> it1() }
            }
        )

        if (!mainActivity.supportFragmentManager.isDestroyed)
            builder.show(mainActivity.supportFragmentManager)
    }

    private fun hideKeyboard() {
        mainActivity.hideKeyboard()
    }

    private fun createCustomTabsServiceConnection() {
        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                customTabsClient: CustomTabsClient
            ) {
                //Pre-warming
                client = customTabsClient
                client?.warmup(0L)
                customTabsSession = client?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                client = null
            }
        }

        CustomTabsClient.bindCustomTabsService(
            this.context,
            CUSTOM_TAB_PACKAGE_NAME,
            customTabsServiceConnection!!
        )
    }

    internal fun showErrorPopup(event: String, onDismiss: () -> Unit) {
        if (errorDialogMsg != event) {
            errorDialogMsg = event
            lifecycleScope.launchWhenResumed {
                showDefaultDialog(event, null) {
                    onDismiss()
                    errorDialogMsg = ""
                }
            }
        }
    }

    protected fun loadContent(url: String, color: Int = Color.TRANSPARENT) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(color)
                .build()
            val customTabsIntent = CustomTabsIntent.Builder()
                .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, params)
                .setShowTitle(false)
                .setUrlBarHidingEnabled(true)
                .build()

            customTabsIntent.launchUrl(context, ConnectionUtils.getUri(url))
        }
    }
}