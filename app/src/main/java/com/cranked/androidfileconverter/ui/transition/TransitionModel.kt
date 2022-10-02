package com.cranked.androidfileconverter.ui.transition

import android.os.Build
import android.os.Parcelable
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.date.DateUtils
import com.cranked.androidfileconverter.utils.file.FileUtility
import kotlinx.parcelize.Parcelize
import java.io.File
import kotlin.streams.toList

@Parcelize
data class TransitionModel(
    val fileName: String,
    val fileExtension: String,
    var fileType: Int,
    val filePath: String,
    val lastModified: String,
    val isFavorite: Boolean,
) : Parcelable

fun File.toTransitionModel(file: File, favoritesDao: FavoritesDao): TransitionModel {
    val fileType = FileUtility.getType(file)
    return TransitionModel(file.name,
        file.extension,
        fileType,
        file.absolutePath,
        DateUtils.getDatefromTime(file.lastModified(), Constants.dateFormat),
        favoritesDao.getFavorite(file.absolutePath, file.name,
            fileType) != null
    )
}

fun TransitionModel.toFavoriteModel(): FavoriteFile {
    return FavoriteFile(this.fileName, this.fileExtension, this.fileType, this.filePath)
}

fun File.toTransitionModel(file: File): TransitionModel {
    val fileType = FileUtility.getType(file)
    return TransitionModel(file.name,
        if (!file.isDirectory) file.extension else "",
        fileType,
        file.absolutePath,
        DateUtils.getDatefromTime(file.lastModified(), Constants.dateFormat),
        false
    )
}

fun List<File>.toTransitionList(favoritesDao: FavoritesDao): List<TransitionModel> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.parallelStream().map { transition ->
            transition.toTransitionModel(transition, favoritesDao)
        }.toList()
    } else {
        this.map { transition ->
            transition.toTransitionModel(transition, favoritesDao)
        }.toList()
    }
}

fun List<File>.toTransitionList(): List<TransitionModel> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.parallelStream().map { transition ->
            transition.toTransitionModel(transition)
        }.toList()
    } else {
        this.map { transition ->
            transition.toTransitionModel(transition)
        }.toList()
    }
}
