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
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentCameraBinding
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CameraFragment @Inject constructor() :
    BaseDaggerFragment<CameraFragmentViewModel, FragmentCameraBinding>(CameraFragmentViewModel::class.java) {
    val app by lazy {
        requireActivity().application as FileConvertApp
    }
    val TAG = this::class.java.toString().substringAfterLast(".")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        app.appComponent.bindCameraFragment(this)
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

        binding.takePhotoButton.setOnClickListener {
            try {
                if (!File(FileUtility.getPhotosPath()).exists()) {
                    if (!File(FileUtility.getPhotosPath()).mkdirs()) {
                        Toast.makeText(activity, requireActivity().baseContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }
                viewModel.takePhoto(requireActivity(), FileUtility.getPhotosPath() + SimpleDateFormat("yyyyMMdd_HHmmss").format(
                    Date()))
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

                    }
                }
            }
        }
    }
}