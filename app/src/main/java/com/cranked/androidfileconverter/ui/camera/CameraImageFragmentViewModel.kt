package com.cranked.androidfileconverter.ui.camera

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.adapter.photo.PhotoFile
import javax.inject.Inject

class CameraImageFragmentViewModel @Inject constructor(private val mContext: Context) : BaseViewModel() {
    private val imagePath = MutableLiveData<PhotoFile>()

    fun sendImagePath(value: PhotoFile) {
        imagePath.postValue(value)
    }

    fun getImagePathMutableLiveData() = this.imagePath
}