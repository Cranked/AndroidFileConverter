package com.cranked.androidfileconverter.ui.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
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
        binding.transitionTextView.text = result
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

    override fun initViewModel(viewModel: TransitionFragmentViewModel) {
        binding.viewModel = viewModel
    }

}