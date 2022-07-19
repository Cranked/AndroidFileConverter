package com.cranked.androidfileconverter.ui.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.transition.TransitionGridAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.databinding.FragmentTransitionBinding
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.junk.ToolbarState

class TransitionFragment :
    BaseDaggerFragment<TransitionFragmentViewModel, FragmentTransitionBinding>(
        TransitionFragmentViewModel::class.java
    ) {
    private val app by lazy {
        activity!!.application as FileConvertApp
    }
    lateinit var path: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        arguments?.let {
            onBundle(it)
        }
        app.rxBus.send(ToolbarState(false))
        init()
        return binding.root
    }

    override fun onBundle(bundle: Bundle) {
        path = bundle.getString(Constants.DESTINATION_PATH_ACTION).toString()
        viewModel.sendPath(path)
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentTransitionBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_transition,
            parent,
            false
        )
    }

    fun init() {
        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> binding.layoutImageView.setImageDrawable(context!!.getDrawable(
                R.drawable.icon_grid))
            LayoutState.GRID_LAYOUT.value -> binding.layoutImageView.setImageDrawable(context!!.getDrawable(
                R.drawable.icon_list))
        }
        binding.layoutImageView.setOnClickListener {
            when (app.getLayoutState()) {
                LayoutState.LIST_LAYOUT.value -> {
                    app.setLayoutState(LayoutState.GRID_LAYOUT.value)
                    binding.layoutImageView.setImageDrawable(context!!.getDrawable(R.drawable.icon_list))
                    viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        TransitionGridAdapter(),
                        viewModel.getFilesFromPath(path, app.getFilterState()))
                }
                LayoutState.GRID_LAYOUT.value -> {
                    app.setLayoutState(LayoutState.LIST_LAYOUT.value)
                    binding.layoutImageView.setImageDrawable(context!!.getDrawable(R.drawable.icon_grid))
                    viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        TransitionListAdapter(),
                        viewModel.getFilesFromPath(path, app.getFilterState()))
                }
            }
        }
    }

    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        viewModel.folderPath.observe(viewLifecycleOwner)
        {
            val list = viewModel.getFilesFromPath(it!!, app.getFilterState())
            viewModel.sendNoDataState(list.size > 0)
            when (app.getLayoutState()) {
                LayoutState.LIST_LAYOUT.value -> {
                    binding.layoutImageView.setImageDrawable(context!!.getDrawable(R.drawable.icon_grid))
                    viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        TransitionListAdapter(),
                        list)
                }
                LayoutState.GRID_LAYOUT.value -> {
                    binding.layoutImageView.setImageDrawable(context!!.getDrawable(R.drawable.icon_list))
                    viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        TransitionGridAdapter(),
                        list)
                }
            }
        }
        viewModel.noDataState.observe(viewLifecycleOwner) {
            binding.emptyFolder.visibility = if (it) View.GONE else View.VISIBLE
            binding.noDataImageView.visibility = if (it) View.GONE else View.VISIBLE
            binding.emptyFolderDescription.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    fun createFolder() {
        viewModel.showCreateFolderBottomDialog(activity!!.supportFragmentManager,
            viewModel.folderPath.value.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createFolderButton.setOnClickListener { createFolder() }
    }

    override fun initViewModel(viewModel: TransitionFragmentViewModel) {
        binding.viewModel = viewModel
    }
}