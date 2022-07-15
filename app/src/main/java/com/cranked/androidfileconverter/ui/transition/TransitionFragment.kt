package com.cranked.androidfileconverter.ui.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.databinding.FragmentTransitionBinding
import com.cranked.androidfileconverter.utils.Constants

class TransitionFragment :
    BaseDaggerFragment<TransitionFragmentViewModel, FragmentTransitionBinding>(
        TransitionFragmentViewModel::class.java
    ) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        arguments?.let {
            onBundle(it)
        }
        return binding.root
    }

    override fun onBundle(bundle: Bundle) {
        val result = bundle.getString(Constants.DESTINATION_PATH_ACTION)
        viewModel.sendPath(result!!)
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

    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        viewModel.folderPath.observe(viewLifecycleOwner)
        {
            val list = viewModel.getFilesFromPath(it!!)
            viewModel.sendNoDataState(list.size > 0)
            viewModel.setAdapter(this.context!!,
                binding.transitionRecylerView, TransitionListAdapter(),
                list)
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