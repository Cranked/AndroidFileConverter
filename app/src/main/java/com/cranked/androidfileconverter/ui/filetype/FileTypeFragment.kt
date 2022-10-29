package com.cranked.androidfileconverter.ui.filetype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentFileTypeBinding
import com.cranked.androidfileconverter.utils.Constants


class FileTypeFragment : BaseDaggerFragment<FileTypeFragmentVM, FragmentFileTypeBinding>(FileTypeFragmentVM::class.java) {

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
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentFileTypeBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_file_type, parent, false)
    }

    override fun initViewModel(viewModel: FileTypeFragmentVM) {
        binding.viewModel = viewModel
    }

    override fun createListeners() {
        binding.selectedItemsLinLayout.setOnClickListener {
            binding.selectedDropDown.setImageDrawable(if (binding.selectedItemsRV.isVisible) ContextCompat.getDrawable(requireContext(),
                R.drawable.down_arrow) else ContextCompat.getDrawable(requireContext(), R.drawable.up_arrow))
            binding.selectedItemsRV.isVisible = !binding.selectedItemsRV.isVisible
        }
    }

    override fun onBundle(bundle: Bundle) {
        val taskType = bundle.getInt(Constants.FILE_TASK_TYPE)
        println(taskType)
    }
}