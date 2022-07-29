package com.cranked.androidfileconverter.ui.task


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.FragmentTaskBinding
import com.cranked.androidfileconverter.ui.main.MainActivity
import com.cranked.androidfileconverter.utils.junk.Title
import javax.inject.Inject

class TaskFragment @Inject constructor() :
    BaseDaggerFragment<TaskFragmentViewModel, FragmentTaskBinding>(TaskFragmentViewModel::class.java) {
    val app by lazy {
        activity!!.application as FileConvertApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(MainActivity::class.java, true)
            }
        })
        arguments?.let {
            onBundle(it)
        }
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentTaskBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_task,
            parent,
            false
        )
    }

    override fun initViewModel(viewModel: TaskFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun createListeners() {
        binding.internalLayout.setOnClickListener {
            viewModel.internalStoragePath(it)
        }
        binding.sdcardLayout.setOnClickListener {
            viewModel.sdCardPathFolder(it)
        }
        binding.downloadsLayout.setOnClickListener {
            viewModel.downloadsPathFolder(it)
        }
        binding.ftLayout.setOnClickListener {
            viewModel.fileTransformerPathFolder(it)
        }
        binding.processedLayout.setOnClickListener {
            viewModel.processedFolderPath(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app.rxBus.send(Title(context!!.getString(R.string.files)))
    }
}