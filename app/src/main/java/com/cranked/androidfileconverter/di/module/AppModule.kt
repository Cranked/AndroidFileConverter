package com.cranked.androidfileconverter.di.module

import android.app.Application
import android.content.Context
import com.cranked.androidcorelibrary.local.LocaleManager
import com.cranked.androidcorelibrary.local.PrefManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): PrefManager {
        return PrefManager(context)
    }

    @Singleton
    @Provides
    fun provideLocaleManager(context: Context): LocaleManager {
        return LocaleManager(provideSharedPreferences(context))
    }


}