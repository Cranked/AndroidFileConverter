package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowItemListTransitionBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class TransitionListAdapter :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowItemListTransitionBinding>(R.layout.row_item_list_transition) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowItemListTransitionBinding,
        position: Int,
    ) {
        binding.transitionFileName.text = getItems()[position].fileName
        binding.lastModifiedTextView.text = getItems()[position].lastModified
        when (getItems()[position].fileType) {
            1 -> {
                binding.transitionImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.icon_folder))
            }
            2 -> {
                binding.transitionImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_default))
            }
        }
        binding.favoriteImageView.visibility = if (getItems()[position].isFavorite) View.VISIBLE else View.GONE
    }

}