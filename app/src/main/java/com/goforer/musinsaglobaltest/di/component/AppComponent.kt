package com.goforer.musinsaglobaltest.di.component

import android.app.Application
import com.goforer.musinsaglobaltest.MusinsaGlobalTest
import com.goforer.musinsaglobaltest.di.module.AppModule
import com.goforer.musinsaglobaltest.di.module.activity.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class, AppModule::class, MainActivityModule::class
    ]
)

interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    fun inject(musinsaGlobalTest: MusinsaGlobalTest)
}