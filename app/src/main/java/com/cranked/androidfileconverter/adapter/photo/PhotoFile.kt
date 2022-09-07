package com.cranked.androidfileconverter.adapter.photo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoFile(val path: String, val fileName: String, val itemCount: Int, val date: String) : Parcelable

data class ImagePreview(val path: String, val fileName: String)