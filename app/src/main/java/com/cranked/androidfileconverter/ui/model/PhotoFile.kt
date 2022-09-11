package com.cranked.androidfileconverter.ui.model

import android.os.Parcelable
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoFile(val path: String, val fileName: String, val itemCount: Int, val date: String) : Parcelable

data class ImagePreview(val path: String, val fileName: String)

fun PhotoFile.toTransitionModel(): TransitionModel {
    return TransitionModel(this.fileName, "", FileType.JPG.type, this.path, "", false)
}