package com.cranked.androidfileconverter.utils.file

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.ui.home.StorageModel
import com.cranked.androidfileconverter.ui.model.PageModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.ui.transition.toTransitionList
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.FileType
import java.io.File
import java.io.FileOutputStream

object FileUtility {
    fun getMenuFolderSizes(context: Context, processedFilesDao: ProcessedFilesDao): StorageModel {
        val base_path = getInternalStoragePath()
        val downloads_path = getDownloadsPath()
        val sdcard_path = getSdCarPath(context)
        val fileTransformerPath = base_path + Constants.folderName
        val fileTransformSize = FileUtils.getFolderFiles(fileTransformerPath, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }.size.toString()
        val internalStorageSize = FileUtils.getFolderFiles(base_path, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }.size.toString()
        val downloadSize = FileUtils.getFolderFiles(downloads_path, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }.size.toString()
        val sdCardSize = FileUtils.getFolderFiles(
            sdcard_path,
            1,
            1
        )
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }.size.toString()
        val processedSize = FileUtils.getFolderFiles(getProcessedPath(), 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }.size.toString()
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

    fun getSdCarPath(context: Context): String {
        return if (FileUtils.isSdCardMounted(context))
            "/storage/" +
                    ContextCompat.getExternalFilesDirs(context, null)[1].absolutePath.split("/")[2] + "/"
        else
            ""
    }

    fun getFileTransformerPath() =
        getInternalStoragePath() + Constants.folderName + File.separator

    fun getDownloadsPath() =
        getInternalStoragePath() + Environment.DIRECTORY_DOWNLOADS + File.separator

    fun getProcessedPath() = getFileTransformerPath() + Constants.processedFolderName + File.separator
    fun getPhotosPath() = getFileTransformerPath() + Constants.photos + File.separator
    fun getType(file: File): Int {
        if (file.isDirectory)
            return FileType.FOLDER.type
        else
            when (file.extension) {
                "pdf" -> return FileType.PDF.type
                "xlxs" -> return FileType.EXCEL.type
                "docx" -> return FileType.WORD.type
                "png" -> return FileType.PNG.type
                "jpg" -> return FileType.JPG.type
            }
        return FileType.OTHERS.type
    }

    fun duplicate(sourcePath: String, targetPath: String): Boolean {
        return File(sourcePath).copyRecursively(File(targetPath))
    }

    fun deleteFile(path: String): Boolean {
        val fileDelete = File(path)
        if (fileDelete.exists()) {
            return fileDelete.canonicalFile.deleteRecursively()
        }
        return true
    }

    fun getAllFilesFromPath(path: String, maxDepth: Int, drop: Int, fileType: String): List<TransitionModel> {
        return FileUtils.getFolderFiles(path, maxDepth, drop).filter { it.extension == fileType }.toMutableList().toTransitionList()
    }

    fun renameFile(oldPath: String, newPath: String): Boolean {
        return File(oldPath).renameTo(File(newPath))
    }

    fun imageToPdf(context: Context, pageModel: PageModel, path: String): Boolean {
        val file = File(path)
        var bitmap = BitmapFactory.decodeFile(path)
        val pdfDocument = PdfDocument()
        val height = context.resources.displayMetrics.densityDpi / 72 * pageModel.pageHeight
        val width = context.resources.displayMetrics.densityDpi / 72 * pageModel.pageWidth
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas!!.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)
        val tempFile = file.path.substringBeforeLast("/")
        val newFileName = file.name.substringBeforeLast(".")
        val resultFile = File(tempFile + File.separator + newFileName + ".pdf")
        try {
            val fileOutputStream = FileOutputStream(resultFile)
            pdfDocument.writeTo(fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        pdfDocument.close()
        return true
    }
}