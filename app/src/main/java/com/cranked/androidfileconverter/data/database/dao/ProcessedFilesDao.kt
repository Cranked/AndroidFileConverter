package com.cranked.androidfileconverter.data.database.dao

import androidx.room.*
import com.cranked.androidfileconverter.data.database.entity.ProcessedFile

@Dao
abstract class ProcessedFilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(processedFiles: List<ProcessedFile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(processedFile: ProcessedFile)

    @Query("SELECT * FROM ${ProcessedFile.TABLE_NAME}")
    abstract fun getAll(): List<ProcessedFile>

    @Query("DELETE FROM ${ProcessedFile.TABLE_NAME}")
    abstract fun deleteAll()

    @Delete
    abstract fun delete(processedFile: ProcessedFile)

    @Update
    abstract fun update(processedFile: ProcessedFile)
}