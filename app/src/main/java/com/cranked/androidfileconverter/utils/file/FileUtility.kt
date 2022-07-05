package com.cranked.androidfileconverter.utils.file

import android.os.Environment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.ui.home.StorageModel

object FileUtility {
    fun getFileSize(): StorageModel {
        val validtypes = arrayListOf("pdf", "xlsx", "docx", "png", "jpeg")
        val base_path = Environment.getExternalStorageDirectory().absolutePath + "/"
        val downloads_path = base_path + Environment.DIRECTORY_DOWNLOADS
        val sdcard_path = System.getenv("EXTERNAL_STORAGE")
        val internalStorageSize = FileUtils.getFolderFiles(base_path, 1, 1)
            .filter { it.isDirectory or validtypes.contains(FileUtils.getExtension(it.name)) }.size.toString()
        val downloadSize = FileUtils.getFolderFiles(downloads_path, 1, 1)
            .filter { it.isDirectory or validtypes.contains(FileUtils.getExtension(it.name)) }.size.toString()
        val sdCardSize = FileUtils.getFolderFiles(
            sdcard_path,
            1,
            1
        )
            .filter { it.isDirectory or validtypes.contains(FileUtils.getExtension(it.name)) }.size.toString()
        val processedSize = "0"
        return StorageModel(
            internalStorageSize,
            sdCardSize,
            downloadSize,
            processedSize
        )
    }
}