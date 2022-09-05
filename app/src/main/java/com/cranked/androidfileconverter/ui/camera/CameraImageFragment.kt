package com.cranked.androidfileconverter.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentCameraImageBinding

class CameraImageFragment :
    BaseDaggerFragment<CameraImageFragmentViewModel, FragmentCameraImageBinding>(CameraImageFragmentViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentCameraImageBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_camera_image, parent, false)
    }

    override fun initViewModel(viewModel: CameraImageFragmentViewModel) {
        binding.viewModel = viewModel
    }
}