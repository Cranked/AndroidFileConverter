package com.cranked.androidfileconverter.ui.imagecrop

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImage.CancelledResult
import com.canhub.cropper.CropImageView
import com.canhub.cropper.common.CommonVersionCheck.isAtLeastM23

class ImageCropPresenter : ImageCropContract.Presenter {

    private var view: ImageCropContract.View? = null
    private val minVersion = isAtLeastM23()
    private val request = false
    private var hasSystemFeature = false
    private var selfPermission = false

    override fun bind(view: ImageCropContract.View?) {
        this.view = view
    }

    override fun unbind() {
        view = null
    }

    override fun onPermissionResult(granted: Boolean) {
        assert(view != null)
        if (granted) {
            view!!.startTakePicture()
        } else if (minVersion && request) {
            view!!.showDialog()
        } else {
            view!!.cameraPermissionLaunch()
        }
    }

    override fun onCreate(context: Context?) {
        assert(view != null)
        if (context == null) {
            view!!.showErrorMessage("onCreate activity and/or context are null")
            return
        }
        hasSystemFeature = if (context.packageManager != null) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        } else {
            false
        }
        selfPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun startWithUriClicked() {
        assert(view != null)
        if (hasSystemFeature && selfPermission) {
            view!!.startTakePicture()
        } else if (hasSystemFeature && minVersion && request) {
            view!!.showDialog()
        } else if (hasSystemFeature) {
            view!!.cameraPermissionLaunch()
        } else {
            view!!.showErrorMessage("onCreate no case apply")
        }
    }

    override fun onOk() {
        assert(view != null)
        view!!.cameraPermissionLaunch()
    }

    override fun onCancel() {
        assert(view != null)
        view!!.showErrorMessage("onCancel")
    }

    override fun onCropImageResult(result: CropImageView.CropResult) {
        if (result.isSuccessful) {
            val SendLink: String = result.uriContent.toString()
            view!!.handleCropImageResult(SendLink, result.originalUri)
        } else if (result == CancelledResult) {
            view!!.showErrorMessage("cropping image was cancelled by the user")
        } else {
            //   view!!.showErrorMessage("cropping image failed");
        }
    }

    override fun onPickImageResult(resultUri: Uri?) {
        if (resultUri != null) {
            Log.v("Uri", resultUri.toString())
            view!!.handleCropImageResult(resultUri.toString(), null)
        } else {
            //  view!!.showErrorMessage("picking image failed");
        }
    }

    override fun onPickImageResultCustom(resultUri: Uri?) {
        if (resultUri != null) {
            Log.v("File Path", resultUri.toString())
            view!!.handleCropImageResult(resultUri.toString(), null)
        } else {
            view!!.showErrorMessage("picking image failed")
        }
    }

    override fun onTakePictureResult(success: Boolean) {
        if (success) {
            view!!.startCropImage()
        } else {
            view!!.showErrorMessage("taking picture failed")
        }
    }
}