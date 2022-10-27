package com.cranked.androidfileconverter.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
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
        val pdfToolsList = viewModel.getPdfToolItems()

        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> {
                val adapter = ToolListAdapter(this)
                binding.pdfConvertersRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter.setItems(pdfConvertersList)
                binding.pdfConvertersRV.adapter = adapter
            }
            LayoutState.GRID_LAYOUT.value -> {
                val pdfConverterAdapter = ToolGridAdapter(this)
                val pdfToolsAdapter = ToolGridAdapter(this)
                val imageConvertersAdapter = ToolGridAdapter(this)
                viewModel.viewModelScope.launch {
                    viewModel.setAdapter(binding.pdfConvertersRV,
                        GridLayoutManager(requireContext(), 3),
                        pdfConverterAdapter,
                        pdfConvertersList)
                    viewModel.setAdapter(binding.pdfToolRV, GridLayoutManager(requireContext(), 3), pdfToolsAdapter, pdfToolsList)
                }
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