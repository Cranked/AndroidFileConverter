package com.cranked.androidfileconverter.ui.filetype

import android.os.Parcelable
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionFileModel(val filePath: String, val fileName: String, var isSelected: Boolean) : Parcelable

fun TransitionModel.toSelectionModel(isSelected: Boolean) =
    SelectionFileModel(this.filePath, this.fileName.substringBeforeLast("."), isSelected)

fun List<TransitionModel>.toSelectionModelList(list: List<SelectionFileModel>?) =
    this.map { transiton ->
        transiton.toSelectionModel(list!!.filter {
            it.filePath == transiton.filePath
        }.isNotEmpty())
    }.toMutableList()