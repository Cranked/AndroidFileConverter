package com.cranked.androidfileconverter.dialog.createfolder

import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.CreateFolderBottomLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import javax.inject.Inject

class CreateFolderBottomDialog @Inject constructor(
    private val transitionFragmentViewModel: TransitionFragmentViewModel,
    private val path: String,
) :
    BaseBottomSheetDialog<CreateFolderBottomLayoutBinding>(R.layout.create_folder_bottom_layout) {
    override fun onBindingCreate(binding: CreateFolderBottomLayoutBinding) {
        binding.createFolderConstraint.setOnClickListener {
            dismiss()
            transitionFragmentViewModel.showDialog(it.context, path)
        }
    }
}