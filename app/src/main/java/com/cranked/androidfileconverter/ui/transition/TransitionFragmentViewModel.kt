package com.cranked.androidfileconverter.ui.transition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import javax.inject.Inject

class TransitionFragmentViewModel @Inject constructor() : BaseViewModel() {
    val path: LiveData<String> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}