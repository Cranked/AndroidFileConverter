package com.cranked.androidfileconverter.adapter.transition

import android.view.View
import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType

class TransitionListAdapter(private val transitionFragmentViewModel: TransitionFragmentViewModel) :
    BaseViewBindingRecyclerViewAdapter<TransitionModel, RowTransitionListItemBinding>(R.layout.row_transition_list_item) {
    override fun setBindingModel(
        item: TransitionModel,
        binding: RowTransitionListItemBinding,
        position: Int,
    ) {
        binding.optionsImageView.visibility =
            if (transitionFragmentViewModel.getLongListenerActivatedMutableLiveData().value!!) View.INVISIBLE else View.VISIBLE
        if (transitionFragmentViewModel.getSelectedRowList().contains(item)) {
            binding.transitionLinearLayout.background =
                binding.root.context.getDrawable(R.drawable.custom_adapter_selected_background)
        } else {
            binding.transitionLinearLayout.background =
                binding.root.context.getDrawable(R.drawable.custom_adapter_unselected_background)
        }
        binding.transitionFileName.text = item.fileName
        binding.lastModifiedTextView.text = item.lastModified
        when (item.fileType) {
            FileType.FOLDER.type -> {
                Glide.with(binding.root.context).load(R.drawable.icon_folder).placeholder(R.drawable.custom_dialog)
                    .into(binding.transitionListImageView)
            }
            FileType.PDF.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_pdf).placeholder(R.drawable.custom_dialog).into(binding.transitionListImageView)
            }
            FileType.PNG.type,FileType.JPG.type -> {
                Glide.with(binding.root.context).load(item.filePath).placeholder(R.drawable.custom_dialog).into(binding.transitionListImageView)
            }
            FileType.WORD.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_doc).placeholder(R.drawable.custom_dialog).into(binding.transitionListImageView)
            }
            FileType.EXCEL.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_psd).placeholder(R.drawable.custom_dialog).into(binding.transitionListImageView)
            }
            FileType.OTHERS.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_default).placeholder(R.drawable.custom_dialog).into(binding.transitionListImageView)
            }
        }
        binding.favoriteImageView.visibility =
            if (item.isFavorite) View.VISIBLE else View.GONE
    }

}