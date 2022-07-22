package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType

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
            FileType.FOLDER.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.icon_folder))
            }
            FileType.PDF.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_pdf))
            }
            FileType.PNG.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_png))
            }
            FileType.JPG.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_jpg))
            }
            FileType.WORD.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_doc))
            }
            FileType.EXCEL.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_psd))
            }
            FileType.OTHERS.type -> {
                binding.transitionListImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_default))
            }
        }
        binding.favoriteImageView.visibility =
            if (item.isFavorite) View.VISIBLE else View.GONE
    }

}