package com.cranked.androidfileconverter.dialog

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.dialog.BaseViewBindingDialogFragment
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.RenameFileLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File

class RenameDialog(
    private val viewModel: TransitionFragmentViewModel,
    private val transitionModel: TransitionModel,
    private val favoritesDao: FavoritesDao
) : BaseViewBindingDialogFragment<RenameFileLayoutBinding>(R.layout.rename_file_layout) {
    override fun onBindingCreate(binding: RenameFileLayoutBinding) {
        getDialog()!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.renameEditText.setText(transitionModel.fileName)
        binding.renameCancelOKLayout.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.renameCancelOKLayout.okButton.setOnClickListener {
            val realPath = transitionModel.filePath.substring(0,
                transitionModel.filePath.lastIndexOf(transitionModel.fileName))
            val editTextString = binding.renameEditText.text.toString()
            if (editTextString.equals(transitionModel.fileName)) {
                dismiss()
                return@setOnClickListener
            }
            if (editTextString.isEmpty()) {
                viewModel.showToast(context!!.getString(R.string.file_name_required))
                return@setOnClickListener
            }
            val existNewFileName =
                File(realPath + binding.renameEditText.text.toString()).exists() and !transitionModel.fileName.equals(
                    editTextString)
            if (existNewFileName) {
                viewModel.showToast(context!!.getString(R.string.file_name_exist))
                return@setOnClickListener
            }
            if (binding.renameEditText.text!!.isNotEmpty()) {
                val newPath = realPath + binding.renameEditText.text.toString()
                val favoriteFile =
                    favoritesDao.getFavorite(transitionModel.filePath,
                        transitionModel.fileName,
                        transitionModel.fileType)
                if (favoriteFile != null) {
                    favoriteFile.fileName = binding.renameEditText.text.toString()
                    favoriteFile.path = newPath
                    favoritesDao.update(favoriteFile)
                }
                val finalFileName = FileUtility.renameFile(transitionModel.filePath,
                    newPath)
               dismiss()
                viewModel.sendItemsChangedSate(true)
            } else {
                viewModel.showToast(context!!.getString(R.string.file_name_required))
            }
        }
    }
}