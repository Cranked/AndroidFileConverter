package com.cranked.androidfileconverter.ui.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.adapter.photo.PhotoAdapter
import com.cranked.androidfileconverter.adapter.photo.PhotoFile
import com.cranked.androidfileconverter.databinding.RowTakenPhotoBinding
import com.cranked.androidfileconverter.utils.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(private val mContext: Context) : BaseViewModel() {
    private lateinit var folderPath: String
    fun takePhoto(fragment: CameraFragment, path: String) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null

        if (intent.resolveActivity(fragment.requireActivity().packageManager) != null) {
            try {
                file = createImageFile(path)
                folderPath = path
            } catch (e: Exception) {
            }
            val uri = FileProvider.getUriForFile(fragment.requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            fragment.startActivityForResult(intent, Constants.RESULT_ADD_PHOTO)
        }
    }

    private fun createImageFile(filePath: String): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val file = File(filePath)
        file.mkdirs()

        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            file /* directory */
        )
    }

    fun setAdapter(
        context: Context,
        activity: Activity,
        recylerView: RecyclerView,
        photoAdapter: PhotoAdapter,
        list: MutableList<PhotoFile>,
    ): PhotoAdapter {
        photoAdapter.apply {
            setItems(list)
            setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<PhotoFile, RowTakenPhotoBinding> {
                override fun onItemClick(item: PhotoFile, position: Int, rowBinding: RowTakenPhotoBinding) {
                    rowBinding.takePhotoLinearLayout.setOnClickListener {
                        println("foto basıldı")
                    }
                }
            })
        }
        recylerView.apply {
            adapter = photoAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        return photoAdapter
    }

    fun getFolderPath() = this.folderPath
}