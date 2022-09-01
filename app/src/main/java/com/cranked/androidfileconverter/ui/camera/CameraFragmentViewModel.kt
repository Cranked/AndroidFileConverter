package com.cranked.androidfileconverter.ui.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.utils.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(private val mContext: Context) : BaseViewModel() {

    fun takePhoto(activity: Activity, path: String) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null
        if (intent.resolveActivity(activity.packageManager) != null) {
            try {
                file = createImageFile(path)
            } catch (e: Exception) {
            }
            val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            activity.startActivityForResult(intent, Constants.RESULT_ADD_PHOTO)
        }
    }

    fun createImageFile(filePath: String): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        val file = File(filePath)
        file.mkdirs()

        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            file /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents

        // Save a file: path for use with ACTION_VIEW intents
        return image
    }
}