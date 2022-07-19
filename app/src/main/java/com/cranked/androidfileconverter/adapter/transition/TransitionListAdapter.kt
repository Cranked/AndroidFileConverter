package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class TransitionListAdapter :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionListItemBinding>(R.layout.row_transition_list_item) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowTransitionListItemBinding,
        position: Int,
    ) {
        binding.transitionFileName.text = item.fileName
        binding.lastModifiedTextView.text = item.lastModified
        when (item.fileType) {
            1 -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.icon_folder))
            }
            2 -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_default))
            }
        }
        binding.favoriteImageView.visibility =
            if (item.isFavorite) View.VISIBLE else View.GONE
    }

}