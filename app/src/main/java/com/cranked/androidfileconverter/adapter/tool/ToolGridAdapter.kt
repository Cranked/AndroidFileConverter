package com.cranked.androidfileconverter.adapter.tool

import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowToolGridItemBinding
import com.cranked.androidfileconverter.ui.tools.ToolModel

class ToolGridAdapter(val listener: ToolListener) :
    BaseViewBindingRecyclerViewAdapter<ToolModel, RowToolGridItemBinding>(R.layout.row_tool_grid_item) {
    override fun setBindingModel(item: ToolModel, binding: RowToolGridItemBinding, position: Int) {
        binding.toolGridImageView.setImageBitmap(item.image)
        binding.toolGridItemName.text = item.taskTypeName
        binding.toolGridLinearLayout.setOnClickListener { listener.onItemClick(item) }
    }
}