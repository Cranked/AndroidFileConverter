package com.cranked.androidfileconverter.ui.task


import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.file.FileUtility
import javax.inject.Inject

class TaskActivityViewModel @Inject constructor() : BaseViewModel() {

    fun moveFiles(list: List<TransitionModel>, newPath: String): Boolean {
        list.forEach {
            val result = FileUtility.renameFile(it.filePath, newPath + it.fileName)
            if (!result) return false
        }
        return true
    }

    fun copyFiles(list: List<TransitionModel>, newPath: String): Boolean {
        list.forEach {
            val result = FileUtility.duplicate(it.filePath, newPath + it.fileName)
            if (!result) return false
        }
        return true
    }


}