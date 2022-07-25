package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionGridItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType

class TransitionGridAdapter (private val transitionFragmentViewModel: TransitionFragmentViewModel) :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionGridItemBinding>(
        R.layout.row_transition_grid_item) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowTransitionGridItemBinding,
        position: Int,
    ) {
        binding.optionsGridImageView.visibility =
            if (transitionFragmentViewModel.longListenerActivated.value!!) View.INVISIBLE else View.VISIBLE
        if (transitionFragmentViewModel.selectedRowList.contains(item)) {
            binding.transitionGridLinearLayout.background =
                binding.root.context.getDrawable(R.drawable.custom_adapter_selected_background)
        } else {
            binding.transitionGridLinearLayout.background =
                binding.root.context.getDrawable(R.drawable.custom_adapter_unselected_background)
        }
        binding.fileNameGridTextView.text = item.fileName
        binding.lastModifiedGridTextView.text = item.lastModified
        when (item.fileType) {
            FileType.FOLDER.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.icon_folder))
            }
            FileType.PDF.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_pdf))
            }
            FileType.PNG.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_png))
            }
            FileType.JPG.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_jpg))
            }
            FileType.WORD.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_doc))
            }
            FileType.EXCEL.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_psd))
            }
            FileType.OTHERS.type -> {
                binding.transitionGridImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    com.cranked.androidcorelibrary.R.drawable.icon_default))
            }
        }
        binding.favoriteGridImageView.visibility = if (item.isFavorite) View.VISIBLE else View.GONE


    }


}