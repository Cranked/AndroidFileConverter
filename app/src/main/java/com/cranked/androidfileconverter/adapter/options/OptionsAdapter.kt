package com.cranked.androidfileconverter.adapter.options

import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowOptionsItemBinding
import com.cranked.androidfileconverter.ui.model.OptionsModel

class OptionsAdapter :
    BaseViewBindingRecyclerViewAdapter<OptionsModel, RowOptionsItemBinding>(R.layout.row_options_item) {
    override fun setBindingModel(
        item: OptionsModel,
        binding: RowOptionsItemBinding,
        position: Int,
    ) {
        binding.optionsNameTextView.text = item.titleName
        binding.optionsImageViewBottom.setImageDrawable(item.drawable)
    }
}