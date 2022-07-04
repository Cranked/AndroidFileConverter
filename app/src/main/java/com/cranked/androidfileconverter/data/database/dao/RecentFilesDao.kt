package com.cranked.androidfileconverter.data.database.dao

import androidx.room.*
import com.cranked.androidfileconverter.data.database.entity.RecentFile

@Dao
abstract class RecentFilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(recentFiles: List<RecentFile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(recentFile: RecentFile)

    @Query("SELECT * FROM ${RecentFile.TABLE_NAME}")
    abstract fun getAll(): List<RecentFile>

    @Query("DELETE FROM ${RecentFile.TABLE_NAME}")
    abstract fun deleteAll()

    @Delete
    abstract fun delete(recentFile: RecentFile)

    @Update
    abstract fun update(recentFile: RecentFile)
}