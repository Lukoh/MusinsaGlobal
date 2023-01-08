package com.goforer.musinsaglobaltest.di.module.fragment.network

import com.goforer.musinsaglobaltest.presentation.ui.network.NetworkStatusFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NetworkStatusFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeNetworkStatusFragment(): NetworkStatusFragment
}