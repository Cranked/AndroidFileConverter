package com.cranked.androidfileconverter.data.database.dao

import androidx.room.*
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile

@Dao
abstract class FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(favorites: List<FavoriteFile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(favoriteFile: FavoriteFile)

    @Query("SELECT * FROM ${FavoriteFile.TABLE_NAME}")
    abstract fun getAll(): List<FavoriteFile>

    @Query("DELETE FROM ${FavoriteFile.TABLE_NAME}")
    abstract fun deleteAll()

    @Query("SELECT * FROM ${FavoriteFile.TABLE_NAME} WHERE ${FavoriteFile.FILE_PATH}=:filePath AND ${FavoriteFile.FILE_NAME}=:fileName AND ${FavoriteFile.FILE_TYPE}=:fileType LIMIT 1 ")
    abstract fun getFavorite(
        filePath: String,
        fileName: String,
        fileType: Int,
    ): FavoriteFile

    @Delete
    abstract fun delete(favoriteFile: FavoriteFile)

    @Update
    abstract fun update(favoriteFile: FavoriteFile)
}