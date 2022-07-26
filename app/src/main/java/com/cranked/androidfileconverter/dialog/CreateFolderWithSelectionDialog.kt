package com.cranked.androidfileconverter.dialog

import android.view.ViewGroup
import com.cranked.androidcorelibrary.dialog.BaseViewBindingDialogFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.CreateFolderDialogLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File

class CreateFolderWithSelectionDialog(
    private val viewModel: TransitionFragmentViewModel,
    private val list: List<TransitionModel>,
    private val path: String,
    private val favoritesDao: FavoritesDao,
) : BaseViewBindingDialogFragment<CreateFolderDialogLayoutBinding>(R.layout.create_folder_dialog_layout) {
    private val TAG = DeleteDialog::class.java.name.toString()

    override fun onBindingCreate(binding: CreateFolderDialogLayoutBinding) {
        try {
            getDialog()!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.createFolderCancelOkLayout.cancelButton.setOnClickListener { dismiss() }
            binding.createFolderCancelOkLayout.okButton.setOnClickListener {
                if (binding.folderNameEditText.text!!.isEmpty()) {
                    viewModel.showToast(context!!.getString(R.string.folder_name_required))
                    return@setOnClickListener
                }
                var folderName =
                    binding.folderNameEditText.text!!.toString()
                val finalCreatedFolder = FileUtils.createfolder(path, folderName)
                folderName = finalCreatedFolder.split("/").last()
                list.forEach { transitionModel ->
                    try {
                        val realPath = finalCreatedFolder.substring(0,
                            finalCreatedFolder.lastIndexOf(transitionModel.fileName))
                        val newPath = realPath + folderName + File.separator + transitionModel.fileName
                        val result = FileUtility.renameFile(transitionModel.filePath, newPath)
                        if (result) {
                            favoritesDao.getAll().forEach {
                                if (it.path.contains(transitionModel.filePath)) {
                                    val tempFilePath = it.path.substring(0, transitionModel.filePath.length)
                                    val result =
                                        realPath + folderName + File.separator + transitionModel.fileName +
                                                it.path.substring(tempFilePath.length)
                                    it.path = result
                                    favoritesDao.update(it)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }
                viewModel.sendPath(path)
                dialog.dismiss()
            }
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }
}