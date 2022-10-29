package com.cranked.androidfileconverter.ui.filetype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import javax.inject.Inject

class FileTypeFragmentVM @Inject constructor() : BaseViewModel() {
    var _mIsVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _mIsVisible

    fun sendVisible(visible: Boolean) {
        _mIsVisible.postValue(visible)
    }
}