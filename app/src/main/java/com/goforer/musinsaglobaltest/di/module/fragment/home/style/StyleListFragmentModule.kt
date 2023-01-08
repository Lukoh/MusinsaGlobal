package com.goforer.musinsaglobaltest.di.module.fragment.home.style

import com.goforer.musinsaglobaltest.presentation.ui.home.HomeFragment
import com.goforer.musinsaglobaltest.presentation.ui.home.style.StyleListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StyleListFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeStyleListFragment(): StyleListFragment
}