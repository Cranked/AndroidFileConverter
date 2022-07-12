package com.cranked.androidfileconverter.utils

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE


object Constants {

    const val folderName = "FileTransformer"
    const val PERMISSION_REQUEST_CODE = 4545
    const val DESTINATION_PATH_ACTION = "DestinationPath"
    val VALID_TYPES = arrayListOf("pdf", "xlsx", "docx", "png", "jpeg")
    var PERMISSIONS = arrayOf(
        CAMERA,
        WRITE_EXTERNAL_STORAGE
    )
}