package com.cranked.androidfileconverter.ui.main

import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel

class MainViewModel : BaseViewModel() {
    val searchVisibleState = MutableLiveData<Boolean>()
    val gridVisibleState = MutableLiveData<Boolean>()
    val userVisibleState = MutableLiveData<Boolean>()
    fun setImageViewsState(value1: Boolean, value2: Boolean, value3: Boolean) {
        searchVisibleState.postValue(value1)
        gridVisibleState.postValue(value2)
        userVisibleState.postValue(value3)
    }

}