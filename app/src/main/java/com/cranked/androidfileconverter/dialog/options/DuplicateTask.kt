package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File

class DuplicateTask(
    private val viewModel: TransitionFragmentViewModel,
    private val transitionList: ArrayList<TransitionModel>,
    private val selectedRowList: ArrayList<TransitionModel>,
) : ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {

        transitionList.forEach { model ->
            if (File(model.filePath).isDirectory) {
                val targetFolderName =
                    FileUtils.createFileAndFolder(model.filePath.substring(0, model.filePath.lastIndexOf("/")),
                        model.fileName)
                FileUtility.duplicate(model.filePath + File.separator, targetFolderName)
            } else {
                val targetFolderName =
                    FileUtils.createFileAndFolder(model.filePath.substring(0, model.filePath.lastIndexOf("/")),
                        model.fileName)

            }
        }
        selectedRowList.clear()
        viewModel.sendLongListenerActivated(false)
    }

    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
    }


}