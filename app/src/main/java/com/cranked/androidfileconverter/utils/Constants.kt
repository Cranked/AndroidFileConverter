package com.cranked.androidfileconverter.utils

import android.Manifest.permission.*


object Constants {

    const val folderName = "FileTransformer"
    const val PERMISSION_REQUEST_CODE = 4545
    var PERMISSIONS = arrayOf(
        CAMERA,
        WRITE_EXTERNAL_STORAGE
    )
}