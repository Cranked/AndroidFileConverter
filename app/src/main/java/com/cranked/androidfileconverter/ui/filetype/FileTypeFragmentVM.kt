package com.cranked.androidfileconverter.ui.filetype

import android.view.animation.AnimationUtils
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
import javax.inject.Inject


class FileTypeFragmentVM @Inject constructor() : BaseViewModel() {
    var _mIsVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _mIsVisible


    fun setAdapter(recyclerView: RecyclerView, recyclerAdapter: SelectionFileListAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.fall_down_layout_animation)
        }
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {


            }


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    fun setAdapter(recyclerView: RecyclerView, recyclerAdapter: SelectionFileGridAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.scale_up_layout_animation)
        }
    }

    fun setAdapter(recyclerView: RecyclerView, selectedFileAdapter: SelectedFileAdapter, selectionFileListener: SelectionFileListener) {
        recyclerView.apply {
            adapter = selectedFileAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

}