package com.cranked.androidfileconverter.ui.task

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.task.TaskListAdapter
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.Constants
import javax.inject.Inject

class TaskTransitionFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val path = MutableLiveData<String>()

    fun sendPath(value: String) {
        path.postValue(value)
    }

    fun setAdapter(
        context: Context, recylerView: RecyclerView, transitionListAdapter: TaskListAdapter,
        list: MutableList<TransitionModel>,
    ): TaskListAdapter {
        transitionListAdapter.apply {
            setItems(list)
            setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowTransitionListItemBinding> {
                override fun onItemClick(item: TransitionModel, position: Int, rowBinding: RowTransitionListItemBinding) {
                    rowBinding.transitionLinearLayout.setOnClickListener {
                        goToTransitionFragmentWithIntent(rowBinding.root, item.filePath)
                    }
                }
            }
            )
        }

        recylerView.apply {
            adapter = transitionListAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        return transitionListAdapter
    }

    fun goToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_taskTransitionFragment_self, bundle)
    }

    fun getPathMutableLiveData() = this.path
}