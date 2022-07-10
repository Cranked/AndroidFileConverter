package com.cranked.androidfileconverter.utils.file

import android.os.Environment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.ui.home.StorageModel
import com.cranked.androidfileconverter.utils.Constants
import java.io.File

object FileUtility {
    fun getMenuFolderSizes(): StorageModel {
        val validtypes = arrayListOf("pdf", "xlsx", "docx", "png", "jpeg")
        val base_path = getInternalStoragePath()
        val downloads_path = getDownloadsPath()
        val sdcard_path = getSdCarPath()
        val fileTransformerPath = base_path + Constants.folderName
        val fileTransformSize = FileUtils.getFolderFiles(fileTransformerPath, 1, 1).size.toString()
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
            fileTransformSize,
            processedSize
        )
    }

    fun getInternalStoragePath() =
        Environment.getExternalStorageDirectory().absolutePath + File.separator

    fun getSdCarPath() = System.getenv("EXTERNAL_STORAGE")
    fun getFileTransformerPath() =
        getInternalStoragePath() + Constants.folderName + File.separator

    fun getDownloadsPath() =
        getInternalStoragePath() + Environment.DIRECTORY_DOWNLOADS + File.separator

    fun getProcessedPath() = getFileTransformerPath() + "processed"
    fun createfolder(path: String, pathName: String) {
        val file = File(path + File.separator + pathName)
        if (!file.exists())
            file.mkdirs()

    }
}