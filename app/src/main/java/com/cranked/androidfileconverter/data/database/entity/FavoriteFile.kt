package com.cranked.androidfileconverter.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "FavoriteFiles")
data class FavoriteFile(
    @ColumnInfo(name = FILE_NAME) val fileName: String,
    @ColumnInfo(name = FILE_EXTENSION) val fileExtension: String,
    @ColumnInfo(name = FILE_TYPE) val fileType: Int,
    @ColumnInfo(name = FILE_PATH) val path: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    companion object {
        const val TABLE_NAME = "FavoriteFiles"
        const val FILE_NAME = "FileName"
        const val FILE_EXTENSION = "FileExtension"
        const val FILE_TYPE = "FileType"
        const val FILE_PATH = "FilePath"
    }

}