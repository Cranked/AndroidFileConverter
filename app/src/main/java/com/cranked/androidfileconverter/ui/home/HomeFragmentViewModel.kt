package com.cranked.androidfileconverter.ui.home

import android.content.Context
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.utils.file.FileUtility
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(val processedFilesDao: ProcessedFilesDao,val mContext: Context) :
    BaseViewModel() {
    val sdCardState = FileUtils.isSdCardMounted(mContext)
    val storageModel = FileUtility.getFileSize()

}