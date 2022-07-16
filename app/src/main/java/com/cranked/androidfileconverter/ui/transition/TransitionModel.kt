package com.cranked.androidfileconverter.ui.transition

import android.os.Parcelable
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
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
    var model = TransitionModel(file.name,
        file.extension,
        fileType,
        file.absolutePath,
        DateUtils.getDatefromTime(file.lastModified(), "dd/MM/yyyy:HH:mm"),
        favoritesDao.getFavorite(file.name,
            fileType) != null
    )
    if (file.isDirectory)
        model.fileType = 1

    return model
}

fun List<File>.toTransitionList(favoritesDao: FavoritesDao): List<TransitionModel> {
    return this.parallelStream().map { transition ->
        transition.toTransitionModel(transition, favoritesDao)
    }.toList()
}
