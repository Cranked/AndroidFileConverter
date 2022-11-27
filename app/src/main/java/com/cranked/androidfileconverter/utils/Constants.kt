package com.cranked.androidfileconverter.utils

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import com.cranked.androidfileconverter.ui.model.PageModel


object Constants {

    const val folderName = "FileTransformer"
    const val processedFolderName = "processed"
    const val photos = "photos"
    const val PERMISSION_REQUEST_CODE = 4545
    const val RESULT_ADD_PHOTO = 4141
    const val DESTINATION_PATH_ACTION = "DestinationPath"
    const val FILE_TASK_TYPE = "FileTaskType"
    const val TAKEN_PHOTO_PATH = "TAKEN_PHOTO_PATH"
    const val IMAGE_CONTENT = "imageContent"
    const val SELECTED_LIST = "SelectedList"
    val VALID_TYPES = arrayListOf("pdf", "xlsx", "docx", "png", "jpg")
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
    val pageSizes: HashMap<String, PageModel> = hashMapOf(
        Pair("A4", PageModel(842, 595)),
        Pair("A3", PageModel(1191, 842)),
        Pair("A2", PageModel(1684, 1191)),
        Pair("A1", PageModel(2384, 1684)),
        Pair("A0", PageModel(3370, 2384))
    )
    const val dateFormat = "dd MMM yyyy HH:mm:ss"
    const val LANGUAGE = "LANGUAGE"
    const val DEFAULT_LANGUAGE = "en"
    const val LAYOUT_STATE = "LayoutState"
    const val FILTER_STATE = "FilterState"
    const val DEFAULT_PAGE_SIZE = "DefaultPageSize"
    const val DEFAULT_PAGE_SIZE_VALUE = "A4"
}

