package com.cranked.androidfileconverter.ui.filetype

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.selectionfile.SelectedFileAdapter
import com.cranked.androidfileconverter.adapter.selectionfile.SelectionFileGridAdapter
import com.cranked.androidfileconverter.adapter.selectionfile.SelectionFileListAdapter
import com.cranked.androidfileconverter.databinding.RowSelectedListItemBinding
import com.cranked.androidfileconverter.databinding.RowSelectionFileGridItemBinding
import com.cranked.androidfileconverter.databinding.RowSelectionFileListItemBinding
import javax.inject.Inject


class FileTypeFragmentVM @Inject constructor() : BaseViewModel() {
    var _mIsVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _mIsVisible


    fun setAdapter(recyclerView: RecyclerView, recyclerAdapter: SelectionFileListAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerAdapter.setListener(object :
            BaseViewBindingRecyclerViewAdapter.ClickListener<SelectionFileModel, RowSelectionFileListItemBinding> {
            override fun onItemClick(item: SelectionFileModel, position: Int, rowBinding: RowSelectionFileListItemBinding) {
                rowBinding.selectionFileListLinLayout.setOnClickListener {
                    item.isSelected = !item.isSelected
                    selectionFileListener.itemSelected(item)
                    rowBinding.selectionFileListLinLayout.setBackgroundColor(if (item.isSelected) rowBinding.root.context.getColor(R.color.item_selected_background_color) else ContextCompat.getColor(
                        rowBinding.root.context,
                        R.color.white))
                }
            }

        })
    }

    fun setAdapter(recyclerView: RecyclerView, recyclerAdapter: SelectionFileGridAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
        recyclerAdapter.setListener(object :
            BaseViewBindingRecyclerViewAdapter.ClickListener<SelectionFileModel, RowSelectionFileGridItemBinding> {
            override fun onItemClick(item: SelectionFileModel, position: Int, rowBinding: RowSelectionFileGridItemBinding) {
                rowBinding.selectionFileGridLinLayout.setOnClickListener {
                    item.isSelected = !item.isSelected
                    selectionFileListener.itemSelected(item)
                    if (item.isSelected) {
                        rowBinding.selectionFileGridLinLayout.setBackgroundColor(rowBinding.root.context.getColor(R.color.item_selected_background_color))
                    } else {
                        rowBinding.selectionFileGridLinLayout.setBackgroundColor(rowBinding.root.context.getColor(R.color.white))
                    }
                }
            }
        })
    }

    fun setAdapter(recyclerView: RecyclerView, selectedFileAdapter: SelectedFileAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = selectedFileAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        selectedFileAdapter.setListener(object :
            BaseViewBindingRecyclerViewAdapter.ClickListener<SelectionFileModel, RowSelectedListItemBinding> {
            override fun onItemClick(item: SelectionFileModel, position: Int, rowBinding: RowSelectedListItemBinding) {
                rowBinding.removeSelectedItem.setOnClickListener {
                    selectionFileListener.itemSelected(item)
                }
            }
        })
    }

}