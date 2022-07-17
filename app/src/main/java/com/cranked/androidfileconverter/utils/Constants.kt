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
    val languagesKeyValue: HashMap<String, String> =
        hashMapOf(
            Pair("tr", "Türkçe"),
            Pair("en", "English"),
            Pair("fr", "français"),
            Pair("es", "espanol"),
            Pair("it", "italiano"),
            Pair("de", "Deutsch"),
            Pair("ja", "日本語"),
            Pair("ko", "한국어")
        )
    const val LANGUAGE = "LANGUAGE"
    const val DEFAULT_LANGUAGE = "en"
    const val LAYOUT_STATE = "LayoutState"
    const val LIST_LAYOUT = "ListLayout"
    const val GRID_LAYOUT = "GridLayout"
}

