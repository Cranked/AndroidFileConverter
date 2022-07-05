package com.cranked.androidfileconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.dao.RecentFilesDao
import com.cranked.androidfileconverter.data.database.dao.ScannedFilesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.data.database.entity.ProcessedFile
import com.cranked.androidfileconverter.data.database.entity.RecentFile
import com.cranked.androidfileconverter.data.database.entity.ScannedFile
import com.cranked.androidfileconverter.internal.DATABASE_NAME

@Database(
    entities = arrayOf(
        FavoriteFile::class,
        ProcessedFile::class,
        RecentFile::class,
        ScannedFile::class
    ), exportSchema = false, version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun processedFilesDao(): ProcessedFilesDao
    abstract fun recentFilesDao(): RecentFilesDao
    abstract fun scannedFiledDao(): ScannedFilesDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()
        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration().build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also { instance = it }
        }
    }

}