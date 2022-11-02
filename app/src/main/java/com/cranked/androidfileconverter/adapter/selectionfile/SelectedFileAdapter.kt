package com.cranked.androidfileconverter.adapter.selectionfile

import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowSelectedListItemBinding
import com.cranked.androidfileconverter.ui.filetype.SelectionFileModel
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class SelectedFileAdapter :
    BaseViewBindingRecyclerViewAdapter<SelectionFileModel, RowSelectedListItemBinding>(R.layout.row_selected_list_item) {
    override fun setBindingModel(item: SelectionFileModel, binding: RowSelectedListItemBinding, position: Int) {
        Glide.with(binding.rowSelectedItemListIV).load(BitmapUtils.getImageByType(binding.root.context, File(item.filePath)))
            .into(binding.rowSelectedItemListIV)
        binding.rowSelectedItemListTV.text = item.fileName
    }
}