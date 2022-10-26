package com.cranked.androidfileconverter.ui.tools

import android.graphics.Bitmap
import com.cranked.androidfileconverter.utils.enums.ToolTaskType

data class ToolModel(val taskTypeName: String, val image: Bitmap, val taskType: ToolTaskType)
