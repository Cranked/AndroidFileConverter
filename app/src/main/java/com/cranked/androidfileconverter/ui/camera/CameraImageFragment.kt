package com.cranked.androidfileconverter.ui.camera

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.photo.PhotoStaggeredAdapter
import com.cranked.androidfileconverter.databinding.FragmentCameraImageBinding
import com.cranked.androidfileconverter.ui.model.ImagePreview
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.file.FileUtility
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File

class CameraImageFragment :
    BaseDaggerFragment<CameraImageFragmentViewModel, FragmentCameraImageBinding>(CameraImageFragmentViewModel::class.java) {
    private val TAG = this::class.java.toString().substringAfterLast(".")
    private val app by lazy {
        requireActivity().application as FileConvertApp
    }
    private val dialog by lazy {
        Dialog(requireContext(), R.style.fullscreenalert)
    }
    private lateinit var folderPath: String
    private lateinit var adapter: PhotoStaggeredAdapter
    private lateinit var path: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        arguments?.let {
            onBundle(it)
        }
        app.rxBus.send(ToolbarState(false))
        viewModel.setTakePhotoAnimationsWithRecyclerView(binding.takePhotoButton, binding.cameraImageRecyclerView)
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentCameraImageBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_camera_image, parent, false)
    }

    override fun initViewModel(viewModel: CameraImageFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onBundle(bundle: Bundle) {
        val photoFile = bundle.get(Constants.IMAGE_CONTENT) as PhotoFile
        viewModel.sendImagePath(photoFile)
    }

    override fun createListeners() {
        try {
            viewModel.getImagePathMutableLiveData().observe(viewLifecycleOwner) {
                path = it.path
                val imageList =
                    FileUtils.getFolderFiles(it.path, 100000, 1).filter { it.isFile and Constants.VALID_TYPES.contains(it.extension) }
                        .map { ImagePreview(it.path, it.name) }.toMutableList()
                binding.cameraToolbarMenu.camImgFileName.text = it.fileName
                adapter =
                    viewModel.setAdapter(this.activity!!,
                        requireActivity().layoutInflater,
                        binding.cameraImageRecyclerView,
                        dialog,
                        PhotoStaggeredAdapter(),
                        imageList)
                println(imageList.toString())
            }
            binding.cameraToolbarMenu.camImgBackButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_cameraImageFragment_to_camera_dest)
            }
            binding.takePhotoButton.setOnClickListener {
                try {
                    if (!File(FileUtility.getPhotosPath()).exists()) {
                        if (!File(FileUtility.getPhotosPath()).mkdirs()) {
                            Toast.makeText(activity,
                                requireActivity().baseContext.getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }
                    }

                    folderPath = BitmapUtils.createImageFile(viewModel.getImagePath(path)).absolutePath
                    println(folderPath)
                    println(path)
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setActivityTitle(getString(R.string.image_crop_title))
                        .setOutputUri(File(folderPath.substringBeforeLast("/")).toUri()).setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle(getString(R.string.finish)).setRequestedSize(400, 400).start(requireContext(), this);

                } catch (e: Exception) {
                    LogManager.log(TAG, e)
                }
            }
        } catch (e: Exception) {
            LogManager.log(TAG, e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        var photoList = FileUtils.getFolderFiles(path, 1, 1).map {
                            ImagePreview(
                                it.path,
                                it.name,
                            )
                        }.toMutableList()
                        adapter.setItems(photoList)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                when (requestCode) {
                    Constants.RESULT_ADD_PHOTO -> FileUtility.deleteFile(folderPath)
                }
            }
        }
    }
}