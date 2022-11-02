package com.cranked.androidfileconverter.adapter.selectionfile

import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowSelectionFileListItemBinding
import com.cranked.androidfileconverter.ui.filetype.SelectionFileListener
import com.cranked.androidfileconverter.ui.filetype.SelectionFileModel
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class SelectionFileListAdapter :
    BaseViewBindingRecyclerViewAdapter<SelectionFileModel, RowSelectionFileListItemBinding>(
        R.layout.row_selection_file_list_item) {
    override fun setBindingModel(item: SelectionFileModel, binding: RowSelectionFileListItemBinding, position: Int) {


        Glide.with(binding.selectionFileListIV).load(BitmapUtils.getImageByType(binding.root.context, File(item.filePath))).into(binding.selectionFileListIV)
        binding.selectionFileNameTV.text = item.fileName
        binding.selectionFileListLinLayout.setBackgroundColor(if (item.isSelected) ContextCompat.getColor(binding.root.context,
            R.color.item_selected_background_color) else ContextCompat.getColor(binding.root.context, R.color.white))
    }
}