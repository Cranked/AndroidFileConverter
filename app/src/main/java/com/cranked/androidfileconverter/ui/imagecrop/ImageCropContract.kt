package com.cranked.androidfileconverter.ui.imagecrop

import android.content.Context
import android.net.Uri
import com.canhub.cropper.CropImageView

interface ImageCropContract {


    interface View {
        fun startCropImage()
        fun showErrorMessage(message: String?)
        fun startTakePicture()
        fun cameraPermissionLaunch()
        fun showDialog()
        fun handleCropImageResult(uri: String?, realUri: Uri?)
    }

    interface Presenter {
        fun bind(view: View?)
        fun unbind()
        fun onPermissionResult(granted: Boolean)
        fun onCreate(context: Context?)
        fun onOk()
        fun onCancel()
        fun onCropImageResult(result: CropImageView.CropResult)
        fun onPickImageResult(resultUri: Uri?)
        fun onPickImageResultCustom(resultUri: Uri?)
        fun onTakePictureResult(success: Boolean)
        fun startWithUriClicked()
    }
}