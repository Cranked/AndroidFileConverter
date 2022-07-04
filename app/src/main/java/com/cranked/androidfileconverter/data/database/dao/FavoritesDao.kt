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

    @Delete
    abstract fun delete(favoriteFile: FavoriteFile)

    @Update
    abstract fun update(favoriteFile: FavoriteFile)
}