package com.cranked.androidfileconverter.ui.main

import android.Manifest
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.ui.model.NavigationModel
import com.cranked.androidfileconverter.ui.search.SearchActivity
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.file.FileUtility
import net.codecision.startask.permissions.Permission
import java.io.File
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainActivity: MainActivity) : BaseViewModel() {
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

    fun setupToolBar(activity: MainActivity, toolbar: Toolbar, visibleState: Boolean) {
        toolbar.setLogo(R.drawable.icon_app_small)
        toolbar.visibility = if (visibleState) View.VISIBLE else View.GONE
        toolbar.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.search_item -> {
                    activity.startActivity(Intent(activity, SearchActivity::class.java))
                    activity.finish()
                }
            }
            true
        }
    }

    fun createFileConverterFolder() {
        try {
            if (!File(Environment.getExternalStorageDirectory().absolutePath + File.separator + Constants.folderName).exists())
                FileUtils.createFileAndFolder(Environment.getExternalStorageDirectory().absolutePath,
                    Constants.folderName)
            if (!File(FileUtility.getProcessedPath()).exists()) {
                FileUtils.createFileAndFolder(FileUtility.getFileTransformerPath(), Constants.processedFolderName)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}