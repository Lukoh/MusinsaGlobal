package com.goforer.musinsaglobaltest.di.module.activity

import com.goforer.musinsaglobaltest.di.module.fragment.home.HomeFragmentModule
import com.goforer.musinsaglobaltest.di.module.fragment.home.style.StyleListFragmentModule
import com.goforer.musinsaglobaltest.di.module.fragment.network.NetworkStatusFragmentModule
import com.goforer.musinsaglobaltest.presentation.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class, StyleListFragmentModule::class,
            NetworkStatusFragmentModule::class
        ]
    )

    abstract fun contributeMainActivity(): MainActivity
}