package com.cranked.androidfileconverter.ui.home

import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.dao.ScannedFilesDao
import com.cranked.androidfileconverter.data.database.entity.ProcessedFile
import com.cranked.androidfileconverter.utils.file.FileUtility
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(val processedFilesDao: ProcessedFilesDao) :
    BaseViewModel() {
    val storageModel = FileUtility.getFileSize()
}