package com.cranked.androidfileconverter.ui.main

import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.ui.model.NavigationModel

class MainViewModel : BaseViewModel() {
    val barIconsVisibleState = MutableLiveData<NavigationModel>()
    fun setImageViewsState(model: NavigationModel) {
        barIconsVisibleState.postValue(model)
    }

    public override  fun onCleared() {
        super.onCleared()
    }
}