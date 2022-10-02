package com.cranked.androidfileconverter.di.module

import android.app.Application
import android.content.Context
import com.cranked.androidcorelibrary.local.LocaleManager
import com.cranked.androidcorelibrary.local.PrefManager
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.ui.model.PageModel
import com.cranked.androidfileconverter.utils.Constants
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

    @Singleton
    @Provides
    fun providesPageModel(application: Application): PageModel {
        val app = application as FileConvertApp
        return Constants.pageSizes[app.getPageDefaultSize()] as PageModel
    }

}