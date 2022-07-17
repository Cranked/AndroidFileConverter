package com.cranked.androidfileconverter.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentSettingsBinding

class SettingsFragment :
    BaseDaggerFragment<SettingsFragmentViewModel, FragmentSettingsBinding>(SettingsFragmentViewModel::class.java) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        arguments?.let {
            onBundle(it)
        }
        binding.languagesConstraintLayout.setOnClickListener {
            viewModel.goToLanguagesActivity(activity!!)
        }
        return binding.root
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentSettingsBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_settings,
            parent,
            false
        )
    }

    override fun initViewModel(viewModel: SettingsFragmentViewModel) {
        binding.viewModel = viewModel
    }

}