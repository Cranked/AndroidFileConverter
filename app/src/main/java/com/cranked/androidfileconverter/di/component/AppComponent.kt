package com.cranked.androidfileconverter.di.component

import android.app.Application
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.di.module.AppModule
import com.cranked.androidfileconverter.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<FileConvertApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): AppComponent?

    }

    fun inject(mainActivity: MainActivity)
    fun inject(application: Application?)
}
