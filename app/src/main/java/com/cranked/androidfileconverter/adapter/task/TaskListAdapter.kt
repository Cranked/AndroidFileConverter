package com.cranked.androidfileconverter.adapter.task

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class TaskListAdapter :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionListItemBinding>(R.layout.row_transition_list_item) {
    override fun setBindingModel(item: TransitionModel, binding: RowTransitionListItemBinding, position: Int) {
        binding.transitionFileName.text = item.fileName
        binding.lastModifiedTextView.text = item.lastModified
        binding.optionsImageView.visibility = View.GONE
        binding.favoriteImageView.visibility = View.GONE
        binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
            R.drawable.icon_folder))
    }
}