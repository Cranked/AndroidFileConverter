package com.cranked.androidfileconverter.ui.home

data class StorageModel(
    val internalStorageSize: String,
    val sdCardSize: String,
    val downloadSize: String,
    val fileTransformFolderSize: String,
    val processedSize: String
)
