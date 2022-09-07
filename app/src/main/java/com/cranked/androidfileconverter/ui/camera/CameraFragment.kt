package com.cranked.androidfileconverter.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.photo.PhotoAdapter
import com.cranked.androidfileconverter.adapter.photo.PhotoFile
import com.cranked.androidfileconverter.databinding.FragmentCameraBinding
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.date.DateUtils
import com.cranked.androidfileconverter.utils.file.FileUtility
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import java.io.File
import javax.inject.Inject


class CameraFragment @Inject constructor() :
    BaseDaggerFragment<CameraFragmentViewModel, FragmentCameraBinding>(CameraFragmentViewModel::class.java) {
    private val app by lazy {
        requireActivity().application as FileConvertApp
    }
    private lateinit var folderPath: String
    private lateinit var adapter: PhotoAdapter
    private lateinit var path: String
    private val TAG = this::class.java.toString().substringAfterLast(".")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        app.appComponent.bindCameraFragment(this)
        app.rxBus.send(ToolbarState(true))

        path = FileUtility.getPhotosPath()
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentCameraBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_camera, parent, false)
    }

    override fun initViewModel(viewModel: CameraFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var photoList = FileUtils.getFolderFiles(path, 1, 1).filter { it.isDirectory }.map {
            PhotoFile(it.path,
                it.name,
                FileUtils.getFolderFiles(it.path, 10000, 1).filter { it.isFile }.size,
                DateUtils.getDatefromTime(it.lastModified(), Constants.dateFormat))
        }.sortedByDescending { it.date }.toMutableList()
        adapter = viewModel.setAdapter(requireActivity().baseContext, binding.recyclerView, PhotoAdapter(), photoList)
        binding.takePhotoButton.setOnClickListener {
            try {
                if (!File(FileUtility.getPhotosPath()).exists()) {
                    if (!File(FileUtility.getPhotosPath()).mkdirs()) {
                        Toast.makeText(activity, requireActivity().baseContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }
                folderPath = viewModel.getImagePath(path)
                BitmapUtils.takePhoto(this, folderPath)
            } catch (e: Exception) {
                LogManager.log(TAG, e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.RESULT_ADD_PHOTO -> {
                        var photoList = FileUtils.getFolderFiles(path, 1, 1).filter { it.isDirectory }.map {
                            PhotoFile(it.path,
                                it.name,
                                FileUtils.getFolderFiles(it.path, 10000, 1).filter { it.isFile }.size,
                                DateUtils.getDatefromTime(it.lastModified(), Constants.dateFormat))
                        }.sortedByDescending { it.date }.toMutableList()
                        adapter.setItems(photoList)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                when (requestCode) {
                    Constants.RESULT_ADD_PHOTO ->
                        FileUtility.deleteFile(folderPath)
                }
            }
        }
    }

}