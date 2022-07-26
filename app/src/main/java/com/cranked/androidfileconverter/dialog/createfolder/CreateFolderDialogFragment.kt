package com.cranked.androidfileconverter.dialog.createfolder

import android.view.ViewGroup
import com.cranked.androidcorelibrary.dialog.BaseViewBindingDialogFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.CreateFolderDialogLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel

class CreateFolderDialogFragment(private val viewModel: TransitionFragmentViewModel, private val path: String) :
    BaseViewBindingDialogFragment<CreateFolderDialogLayoutBinding>(R.layout.create_folder_dialog_layout) {

    override fun onBindingCreate(binding: CreateFolderDialogLayoutBinding) {
        getDialog()!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.createFolderCancelOkLayout.cancelButton.setOnClickListener { dismiss() }
        binding.createFolderCancelOkLayout.okButton.setOnClickListener {
            if (binding.folderNameEditText.text!!.isEmpty()) {
                viewModel.showToast(context!!.getString(R.string.folder_name_required))
                return@setOnClickListener
            }
            val folderName =
                binding.folderNameEditText.text!!.toString()
            FileUtils.createfolder(path, folderName)
            viewModel.sendPath(path)
            dialog.dismiss()
        }
    }
}