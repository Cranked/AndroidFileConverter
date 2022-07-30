package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionGridItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType

class TransitionGridAdapter(private val transitionFragmentViewModel: TransitionFragmentViewModel) :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionGridItemBinding>(
        R.layout.row_transition_grid_item) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowTransitionGridItemBinding,
        position: Int,
    ) {
        binding.optionsGridImageView.visibility =
            if (transitionFragmentViewModel.getLongListenerActivatedMutableLiveData().value!!) View.INVISIBLE else View.VISIBLE

        if (transitionFragmentViewModel.getSelectedRowList().contains(item)) {
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
                Glide.with(binding.root.context).load(R.drawable.icon_folder).placeholder(R.drawable.custom_dialog)
                    .apply(RequestOptions().transform(
                        RoundedCorners(10)))
                    .into(binding.transitionGridImageView)
            }
            FileType.PDF.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_pdf)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.transitionGridImageView)

            }
            FileType.PNG.type, FileType.JPG.type -> {
                Glide.with(binding.root.context).load(item.filePath).placeholder(R.drawable.custom_dialog)
                    .apply(RequestOptions().transform(RoundedCorners(10))).into(binding.transitionGridImageView)
            }
            FileType.WORD.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_doc)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.transitionGridImageView)

            }
            FileType.EXCEL.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_psd)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.transitionGridImageView)
            }
            FileType.OTHERS.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_default)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.transitionGridImageView)
            }
        }
        binding.favoriteGridImageView.visibility = if (item.isFavorite) View.VISIBLE else View.GONE


    }


}