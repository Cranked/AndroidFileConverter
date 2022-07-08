package com.cranked.androidfileconverter.ui.main

import android.Manifest
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.ui.model.NavigationModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.file.FileUtility
import net.codecision.startask.permissions.Permission
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {
    val TAG = MainViewModel::class.java.name.toString()
    val barIconsVisibleState = MutableLiveData<NavigationModel>()
    fun setImageViewsState(model: NavigationModel) {
        barIconsVisibleState.postValue(model)
    }

    val permissionTemp by lazy {
        Permission.Builder(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).setShouldShowRationale(true)
            .setShouldRequestAutomatically(true)
            .setRequestCode(Constants.PERMISSION_REQUEST_CODE)
            .build()
    }

    fun init(activity: MainActivity) = try {

        if (permissionTemp.isGranted(activity))
            createFileConverterFolder()
        else
            permissionTemp.request(activity)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }


    fun createFileConverterFolder() = try {
        FileUtility.createfolder(
            Environment.getExternalStorageDirectory().absolutePath,
            Constants.folderName
        )
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }

    public override fun onCleared() {
        super.onCleared()
    }

}