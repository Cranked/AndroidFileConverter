package com.cranked.androidfileconverter.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.tool.ToolGridAdapter
import com.cranked.androidfileconverter.adapter.tool.ToolListAdapter
import com.cranked.androidfileconverter.adapter.tool.ToolListener
import com.cranked.androidfileconverter.databinding.FragmentToolsBinding
import com.cranked.androidfileconverter.utils.enums.LayoutState
import javax.inject.Inject

class ToolsFragment @Inject constructor() :
    BaseDaggerFragment<ToolsFragmentViewModel, FragmentToolsBinding>(ToolsFragmentViewModel::class.java), ToolListener {
    val app by lazy {
        requireActivity().application as FileConvertApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentToolsBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tools, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pdfConvertersList = viewModel.getPdfConverterItems()
        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> {
                val adapter = ToolListAdapter(this)
                binding.pdfConvertersRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter.setItems(pdfConvertersList)
                binding.pdfConvertersRV.adapter = adapter
            }
            LayoutState.GRID_LAYOUT.value -> {
                val adapter = ToolGridAdapter(this)
                binding.pdfConvertersRV.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter.setItems(pdfConvertersList)
                binding.pdfConvertersRV.adapter = adapter
            }
        }
    }


    override fun initViewModel(viewModel: ToolsFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onItemClick(item: ToolModel) {
        println(item)

    }
}