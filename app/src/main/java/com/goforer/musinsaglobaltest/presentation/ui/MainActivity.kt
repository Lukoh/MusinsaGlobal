package com.goforer.musinsaglobaltest.presentation.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.goforer.base.extension.safeNavigate
import com.goforer.base.extension.setSystemBarTextDark
import com.goforer.base.view.dialog.NormalDialog
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaGlobalTest.databinding.ActivityMainBinding
import com.goforer.musinsaglobaltest.presentation.Caller.NOT_AVAILABLE_NETWORK_INTERNET
import com.goforer.musinsaglobaltest.presentation.ui.home.HomeFragmentDirections
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class MainActivity : AppCompatActivity(), HasAndroidInjector {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var notAvailableNetwork = false

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (::navController.isInitialized) {
                    val navHostFragment = supportFragmentManager.fragments.first()

                    if (navHostFragment != null && navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                        moveTaskToBack(true)
                    } else {
                        navHostFragment?.childFragmentManager?.fragments?.let {
                            if (it.isNotEmpty()) {
                                if (navController.popBackStack().not())
                                    moveTaskToBack(true)
                                else
                                    onBackPressedDispatcher.onBackPressed()
                            } else
                                showDefaultDialog("Something Wrong")
                        }
                    }
                } else
                    handleOnBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        window.setSystemBarTextDark()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        notAvailableNetwork = intent.getBooleanExtra(NOT_AVAILABLE_NETWORK_INTERNET, false)
        init()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        }

        handleNotAvailableNetwork(binding)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (::navController.isInitialized && ::appBarConfiguration.isInitialized)
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        else
            super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::navController.isInitialized) {
            val navHostFragment = supportFragmentManager.fragments.first()

            if (navHostFragment != null && navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                moveTaskToBack(true)
            } else {
                navHostFragment?.childFragmentManager?.fragments?.let {
                    if (it.isNotEmpty()) {
                        if (navController.popBackStack().not())
                            moveTaskToBack(true)
                        else
                            onBackPressedDispatcher.onBackPressed()
                    } else
                        showDefaultDialog("Something Wrong")
                }
            }
        } else
            handleOnBackPressed()
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (::binding.isInitialized) {
            with(binding) {
                setContentView(root)
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayShowTitleEnabled(false)
                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_container) as NavHostFragment
                navController = navHostFragment.navController
                appBarConfiguration = AppBarConfiguration(navController.graph)
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
        }
    }

    private fun handleNotAvailableNetwork(binding: ActivityMainBinding) {
        if (notAvailableNetwork) {
            if (::navController.isInitialized) {
                val direction =
                    HomeFragmentDirections.actionHomeFragmentToNetworkDisconnectFragment()

                navController.safeNavigate(direction.actionId, direction)
            }
        }
    }

    private fun handleOnBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressedDispatcher.onBackPressed()

            return
        }

        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    internal fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        currentFocus?.windowToken?.let {
            inputManager.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    internal fun showDefaultDialog(
        message: CharSequence, title: CharSequence? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        val builder = NormalDialog.Builder()

        builder.setContext(this)
        builder.setHorizontalMode(true)
        title?.let {
            builder.setTitle(title)
        }
        builder.setMessage(message)
        builder.setPositiveButton(R.string.confirm) { _, _ ->
        }

        builder.setOnDismissListener(
            DialogInterface.OnDismissListener {
                onDismiss?.let { it1 -> it1() }
            }
        )
        if (!supportFragmentManager.isDestroyed)
            builder.show(supportFragmentManager)
    }
}