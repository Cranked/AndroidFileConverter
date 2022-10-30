package com.cranked.androidfileconverter.ui.filetype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.selectionfile.SelectionFileGridAdapter
import com.cranked.androidfileconverter.adapter.selectionfile.SelectionFileListAdapter
import com.cranked.androidfileconverter.databinding.FragmentFileTypeBinding
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.enums.ToolTaskType
import com.cranked.androidfileconverter.utils.file.FileUtility
import io.reactivex.rxjava3.disposables.Disposable


class FileTypeFragment : BaseDaggerFragment<FileTypeFragmentVM, FragmentFileTypeBinding>(FileTypeFragmentVM::class.java),
    SelectionFileListener {
    var selectedItemsList = arrayListOf<SelectionFileModel>()
    val app by lazy {
        requireActivity().application as FileConvertApp
    }
    val listAdapter = SelectionFileListAdapter()
    val gridAdapter = SelectionFileGridAdapter()
    lateinit var disposable: Disposable

    lateinit var selectionFileList: MutableList<SelectionFileModel>

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
            binding.selectedItemsRV.isVisible = !binding.selectedItemsRV.isVisible
            binding.selectedDropDown.setImageDrawable(if (binding.selectedItemsRV.isVisible) ContextCompat.getDrawable(requireContext(),
                R.drawable.icon_down_arrow) else ContextCompat.getDrawable(requireContext(), R.drawable.icon_up_arrow))
        }
    }

    override fun onBundle(bundle: Bundle) {
        val taskType = bundle.getInt(Constants.FILE_TASK_TYPE)
        bundle.getParcelableArrayList<SelectionFileModel>(Constants.SELECTED_LIST)?.let {
            selectedItemsList.addAll(it)
        }
        when (taskType) {
            ToolTaskType.PDFTOWORD.value,
            ToolTaskType.PDFTOIMAGES.value,
            ToolTaskType.PDFTOEXCEL.value,
            ToolTaskType.UNLOCKPDF.value,
            ToolTaskType.LOCKPDF.value,
            ToolTaskType.COMPRESSPDF.value,
            ToolTaskType.MERGEPDF.value,
            ToolTaskType.SPLITPDF.value,
            -> {
                selectionFileList = FileUtility.getAllFilesFromPath(FileUtility.getInternalStoragePath(), 1000, 1, "pdf")
                    .toSelectionModelList(selectedItemsList)

            }
        }
        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> {
                listAdapter.setItems(selectionFileList)
                viewModel.setAdapter(binding.fileTypeResultRV, listAdapter, this)
            }
            LayoutState.GRID_LAYOUT.value -> {
                gridAdapter.setItems(selectionFileList)
                viewModel.setAdapter(binding.fileTypeResultRV, gridAdapter, this)
            }
        }
        println(taskType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            onBundle(it)
        }
    }

    override fun itemSelected(selectionFileModel: SelectionFileModel) {
        selectionFileList.forEach { selectionFile ->
            if (selectedItemsList.filter {
                    it.filePath == selectionFile.filePath
                }.isNotEmpty()) {
                selectedItemsList.remove(selectionFile)
            } else {
                selectedItemsList.add(selectionFile)
            }
        }
    }

    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        disposable = app.rxBus.toObservable().subscribe {
            when (it) {
                LayoutState.LIST_LAYOUT.value -> {
                    listAdapter.setItems(selectionFileList)
                    viewModel.setAdapter(binding.fileTypeResultRV, listAdapter, this)
                }
                LayoutState.GRID_LAYOUT.value -> {
                    gridAdapter.setItems(selectionFileList)
                    viewModel.setAdapter(binding.fileTypeResultRV, gridAdapter, this)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBindingClear(binding)
        disposable.dispose()
    }
}