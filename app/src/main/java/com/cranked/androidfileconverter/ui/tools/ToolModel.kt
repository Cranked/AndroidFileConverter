package com.cranked.androidfileconverter.ui.tools

import android.graphics.Bitmap
import android.os.Parcelable
import com.cranked.androidfileconverter.utils.enums.ToolTaskType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToolModel(val taskTypeName: String, val image: Bitmap, val taskType: ToolTaskType) : Parcelable
