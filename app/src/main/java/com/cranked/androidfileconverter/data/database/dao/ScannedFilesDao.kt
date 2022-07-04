package com.cranked.androidfileconverter.data.database.dao

import androidx.room.*
import com.cranked.androidfileconverter.data.database.entity.ScannedFile


@Dao
abstract class ScannedFilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(scannedFiles: List<ScannedFile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(scannedFile: ScannedFile)

    @Query("SELECT * FROM ${ScannedFile.TABLE_NAME}")
    abstract fun getAll(): List<ScannedFile>

    @Query("DELETE FROM ${ScannedFile.TABLE_NAME}")
    abstract fun deleteAll()

    @Delete
    abstract fun delete(scannedFile: ScannedFile)

    @Update
    abstract fun update(scannedFile: ScannedFile)
}