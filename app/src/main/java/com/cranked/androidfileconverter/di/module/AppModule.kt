package com.cranked.androidfileconverter.di.module

import android.app.Application
import android.content.Context
import com.cranked.androidfileconverter.data.database.AppDataBase
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
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

}