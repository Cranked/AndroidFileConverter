package com.cranked.androidfileconverter.ui.task

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.date.DateUtils
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File
import javax.inject.Inject

class TaskFragmentViewModel @Inject constructor( private val mContext: Context) :
    BaseViewModel() {

    val sdCardState = FileUtils.isSdCardMounted(mContext)
    val internalLastModified = getLastModified(FileUtility.getInternalStoragePath())
    val sdLastModified = getLastModified(FileUtility.getSdCarPath(mContext))
    val downloadsLastModified = getLastModified(FileUtility.getDownloadsPath())
    val fileTransformerLastModified = getLastModified(FileUtility.getFileTransformerPath())
    val processedLastModified = getLastModified(FileUtility.getProcessedPath())
    fun goToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_taskFragment_to_taskTransitionFragment, bundle)
    }

    fun getLastModified(path: String) = DateUtils.getDatefromTime(File(path).lastModified(), Constants.dateFormat)
    fun internalStoragePath(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getInternalStoragePath())

    fun sdCardPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getSdCarPath(view.context))

    fun downloadsPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getDownloadsPath())

    fun fileTransformerPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getFileTransformerPath())

    fun processedFolderPath(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getProcessedPath())
}
