package com.cranked.androidfileconverter.ui.home

import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.utils.file.FileUtility

class HomeFragmentViewModel() : BaseViewModel() {
    val storageModel = FileUtility.getFileSize()
    public override fun onCleared() {
        super.onCleared()
    }
}