package com.cranked.androidfileconverter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentHomeBinding

class HomeFragment :
    BaseDaggerFragment<HomeFragmentViewModel, FragmentHomeBinding>(HomeFragmentViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        return binding.root
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentHomeBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, parent, false)
    }

    override fun initViewModel(viewModel: HomeFragmentViewModel) {
        binding.viewModel = viewModel
    }

}