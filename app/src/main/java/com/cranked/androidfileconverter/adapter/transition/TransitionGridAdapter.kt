package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionGridItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class TransitionGridAdapter :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionGridItemBinding>(
        R.layout.row_transition_grid_item) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowTransitionGridItemBinding,
        position: Int,
    ) {
        binding.fileNameGridTextView.text = getItems()[position].fileName
        binding.lastModifiedGridTextView.text = getItems()[position].lastModified
        when (getItems()[position].fileType) {
            1 -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.icon_folder))
            }
            2 -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_default))
            }
        }
        binding.favoriteGridImageView.visibility = if (item.isFavorite) View.VISIBLE else View.GONE


    }


}