package com.cranked.androidfileconverter.ui.imagecrop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.utils.Constants
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.IOException

class ImageCropActivity : AppCompatActivity(), ImageCropContract.View {
    private val presenter: ImageCropContract.Presenter = ImageCropPresenter()
    private val cropImage =
        registerForActivityResult(CropImageContract(), presenter::onCropImageResult)
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture(), presenter::onTakePictureResult)
    lateinit var photoUri: Uri
    private lateinit var options: CropImageContractOptions

    private val AUTHORITY_SUFFIX = ".fileprovider"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)
        val photoPath = intent.extras!!.getString(Constants.TAKEN_PHOTO_PATH, "")
        photoPath?.let {
            photoUri = File(it).toUri()
        }
        presenter.bind(this)
        presenter.onCreate(this)
        options = getOptions()
        cropImage.launch(options)

    }

    override fun startCropImage() {
        TODO("Not yet implemented")
    }

    override fun showErrorMessage(message: String?) {
        TODO("Not yet implemented")
    }

    override fun startTakePicture() {
        try {
            val ctx: Context = this
            val authorities = ctx.packageName + this.AUTHORITY_SUFFIX
            //photoUri = FileProvider.getUriForFile(ctx, authorities, createImageFile());
            photoUri = Uri.fromFile(
                File.createTempFile(
                    System.currentTimeMillis().toString() + "_", ".jpg",
                    cacheDir
                )
            )
            takePicture.launch(photoUri)
        } catch (e: IOException) {
            e.printStackTrace()
            Firebase.crashlytics.recordException(e)
        }
    }

    override fun cameraPermissionLaunch() {
        TODO("Not yet implemented")
    }

    override fun showDialog() {
        TODO("Not yet implemented")
    }

    override fun handleCropImageResult(uri: String?, realUri: Uri?) {
        TODO("Not yet implemented")
    }

    private fun getOptions(): CropImageContractOptions {
        return CropImageContractOptions(photoUri, CropImageOptions())
            .setScaleType(CropImageView.ScaleType.FIT_CENTER)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .setAspectRatio(1, 1)
            .setMaxZoom(4)
            .setAutoZoomEnabled(true)
            .setMultiTouchEnabled(true)
            .setCenterMoveEnabled(true)
            .setShowCropOverlay(true)
            .setAllowFlipping(true)
            .setSnapRadius(3f)
            .setTouchRadius(48f)
            .setInitialCropWindowPaddingRatio(0.1f)
            .setBorderLineThickness(3f)
            .setBorderLineColor(Color.argb(170, 255, 255, 255))
            .setBorderCornerThickness(2f)
            .setBorderCornerOffset(5f)
            .setBorderCornerLength(14f)
            .setBorderCornerColor(Color.WHITE)
            .setGuidelinesThickness(1f)
            .setGuidelinesColor(R.color.white)
            .setBackgroundColor(Color.argb(119, 0, 0, 0))
            .setMinCropWindowSize(24, 24)
            .setMinCropResultSize(20, 20)
            .setMaxCropResultSize(99999, 99999)
            .setActivityTitle("")
            .setActivityMenuIconColor(0)
            .setOutputUri(photoUri)
            .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
            .setOutputCompressQuality(90)
            .setRequestedSize(0, 0)
            .setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
            .setInitialCropWindowRectangle(null)
            .setInitialRotation(90)
            .setAllowCounterRotation(false)
            .setFlipHorizontally(false)
            .setFlipVertically(false)
            .setCropMenuCropButtonTitle(null)
            .setCropMenuCropButtonIcon(0)
            .setAllowRotation(true)
            .setNoOutputImage(false)
            .setFixAspectRatio(false)
    }

    private fun startCameraWithUri() {
        var options: CropImageContractOptions? = null
        options = CropImageContractOptions(photoUri, CropImageOptions())
            .setScaleType(CropImageView.ScaleType.FIT_CENTER)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .setAspectRatio(1, 1)
            .setMaxZoom(4)
            .setAutoZoomEnabled(true)
            .setMultiTouchEnabled(true)
            .setCenterMoveEnabled(true)
            .setShowCropOverlay(true)
            .setAllowFlipping(true)
            .setSnapRadius(3f)
            .setTouchRadius(48f)
            .setInitialCropWindowPaddingRatio(0.1f)
            .setBorderLineThickness(3f)
            .setBorderLineColor(Color.argb(170, 255, 255, 255))
            .setBorderCornerThickness(2f)
            .setBorderCornerOffset(5f)
            .setBorderCornerLength(14f)
            .setBorderCornerColor(Color.WHITE)
            .setGuidelinesThickness(1f)
            .setGuidelinesColor(R.color.white)
            .setBackgroundColor(Color.argb(119, 0, 0, 0))
            .setMinCropWindowSize(24, 24)
            .setMinCropResultSize(20, 20)
            .setMaxCropResultSize(99999, 99999)
            .setActivityTitle("")
            .setActivityMenuIconColor(0)
            .setOutputUri(null)
            .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
            .setOutputCompressQuality(90)
            .setRequestedSize(0, 0)
            .setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
            .setInitialCropWindowRectangle(null)
            .setInitialRotation(90)
            .setAllowCounterRotation(false)
            .setFlipHorizontally(false)
            .setFlipVertically(false)
            .setCropMenuCropButtonTitle(null)
            .setCropMenuCropButtonIcon(0)
            .setAllowRotation(true)
            .setNoOutputImage(false)
            .setFixAspectRatio(false)
        cropImage.launch(options)
    }
}