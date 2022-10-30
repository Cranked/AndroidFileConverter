package com.cranked.androidfileconverter.adapter.selectionfile

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowSelectionFileGridItemBinding
import com.cranked.androidfileconverter.ui.filetype.SelectionFileModel
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class SelectionFileGridAdapter :
    BaseViewBindingRecyclerViewAdapter<SelectionFileModel, RowSelectionFileGridItemBinding>(R.layout.row_selection_file_grid_item) {
    override fun setBindingModel(item: SelectionFileModel, binding: RowSelectionFileGridItemBinding, position: Int) {
        binding.selectionFileGridTV.text = item.fileName
        Glide.with(binding.selectionFileGridIV).load(BitmapUtils.getImageByType(binding.root.context, File(item.filePath)))
            .into(binding.selectionFileGridIV)
        binding.selectionFileGridLinLayout.setBackgroundColor(if (item.isSelected) binding.root.context.getColor(R.color.item_selected_background_color) else ContextCompat.getColor(
            binding.root.context,
            R.color.white))
    }
}