package com.cranked.androidfileconverter.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.tool.ToolGridAdapter
import com.cranked.androidfileconverter.adapter.tool.ToolListAdapter
import com.cranked.androidfileconverter.adapter.tool.ToolListener
import com.cranked.androidfileconverter.databinding.FragmentToolsBinding
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class ToolsFragment @Inject constructor() :
    BaseDaggerFragment<ToolsFragmentViewModel, FragmentToolsBinding>(ToolsFragmentViewModel::class.java), ToolListener {
    val app by lazy {
        requireActivity().application as FileConvertApp
    }
    lateinit var disposable: Disposable

    lateinit var pdfConvertersList: ArrayList<ToolModel>
    lateinit var pdfToolsList: ArrayList<ToolModel>

    var converterListAdapter = ToolListAdapter(this)
    var toolListAdapter = ToolListAdapter(this)
    var imgListAdapter = ToolListAdapter(this)

    var converterGridAdapter = ToolGridAdapter(this)
    var toolGridAdapter = ToolGridAdapter(this)
    var imgGridAdapter = ToolGridAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        app.rxBus.send(ToolbarState(true))
        pdfConvertersList = viewModel.getPdfConverterItems()
        pdfToolsList = viewModel.getPdfToolItems()
        viewModel.init(app,
            requireContext(),
            binding,
            converterListAdapter,
            converterGridAdapter,
            toolListAdapter,
            toolGridAdapter,
            pdfConvertersList,
            pdfToolsList)

        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentToolsBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tools, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        app.rxBus.toObservable().subscribe {
            when (it) {
                LayoutState.LIST_LAYOUT.value, LayoutState.GRID_LAYOUT.value -> {
                    viewModel.init(app,
                        requireContext(),
                        binding,
                        converterListAdapter,
                        converterGridAdapter,
                        toolListAdapter,
                        toolGridAdapter,
                        pdfConvertersList,
                        pdfToolsList)
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