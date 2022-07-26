package com.cranked.androidfileconverter.di.module

import android.app.Application
import androidx.room.Room
import com.cranked.androidfileconverter.data.database.AppDataBase
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.dao.RecentFilesDao
import com.cranked.androidfileconverter.data.database.dao.ScannedFilesDao
import com.cranked.androidfileconverter.internal.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(applicationContext: Application): AppDataBase {
        return Room.databaseBuilder(applicationContext, AppDataBase::class.java, DATABASE_NAME)
            .allowMainThreadQueries().fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideprocessFilesDao(dataBase: AppDataBase): ProcessedFilesDao {
        return dataBase.processedFilesDao()
    }

    @Singleton
    @Provides
    fun provideScannedFilesDao(dataBase: AppDataBase): ScannedFilesDao {
        return dataBase.scannedFiledDao()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(dataBase: AppDataBase): FavoritesDao {
        return dataBase.favoritesDao()
    }

    @Singleton
    @Provides
    fun provideRecentFilesDao(dataBase: AppDataBase): RecentFilesDao {
        return dataBase.recentFilesDao()
    }
}