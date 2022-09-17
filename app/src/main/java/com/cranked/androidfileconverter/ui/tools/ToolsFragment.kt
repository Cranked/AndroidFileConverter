package com.cranked.androidfileconverter.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentToolsBinding
import com.cranked.androidfileconverter.ui.tools.tasks.ImgToPdfTask
import javax.inject.Inject

class ToolsFragment @Inject constructor() :
    BaseDaggerFragment<ToolsFragmentViewModel, FragmentToolsBinding>(ToolsFragmentViewModel::class.java) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onBundle(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        val imgToPdfTask = ImgToPdfTask(emptyList())

        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentToolsBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tools, parent, false)
    }

    override fun initViewModel(viewModel: ToolsFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onBundle(bundle: Bundle) {
        
    }
}