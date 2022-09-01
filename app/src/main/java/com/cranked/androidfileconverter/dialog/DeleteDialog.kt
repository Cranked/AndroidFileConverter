package com.cranked.androidfileconverter.dialog

import android.view.ViewGroup
import com.cranked.androidcorelibrary.dialog.BaseViewBindingDialogFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.DialogDeleteFilesBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.file.FileUtility

class DeleteDialog(
    private val viewModel: TransitionFragmentViewModel,
    private val list: ArrayList<TransitionModel>,
    private val favoritesDao: FavoritesDao,
) :
    BaseViewBindingDialogFragment<DialogDeleteFilesBinding>(R.layout.dialog_delete_files) {
    private val TAG = DeleteDialog::class.java.name.toString()
    override fun onBindingCreate(binding: DialogDeleteFilesBinding) {
        try {
            getDialog()!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.deleteContentDescription.text = requireContext().getString(R.string.wantToDeleteFile)
            binding.deleteDialogLayout.cancelButton.setOnClickListener {
                dismiss()
                viewModel.getSelectedRowList().clear()
                viewModel.sendLongListenerActivated(false)
                viewModel.sendItemsChangedSate(true)
            }
            binding.deleteDialogLayout.okButton.setOnClickListener {
                list.forEach { transition ->
                    FileUtils.getFolderFiles(transition.filePath, 10000, 0).forEach {
                        if (FileUtility.deleteFile(it.path)) {
                            val favoriteFile =
                                favoritesDao.getFavorite(it.path, it.name, transition.fileType)
                            if (favoriteFile != null) {
                                favoritesDao.delete(favoriteFile)
                            }
                        }
                    }
                }
                viewModel.getSelectedRowList().clear()
                viewModel.getItemsChangedStateMutableLiveData().postValue(true)
                viewModel.sendLongListenerActivated(false)
                dismiss()
            }
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }
}