package com.cranked.androidfileconverter.adapter.tool

import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowToolListItemBinding
import com.cranked.androidfileconverter.ui.tools.ToolModel

class ToolListAdapter(val listener: ToolListener) :
    BaseViewBindingRecyclerViewAdapter<ToolModel, RowToolListItemBinding>(R.layout.row_tool_list_item) {
    override fun setBindingModel(item: ToolModel, binding: RowToolListItemBinding, position: Int) {
        binding.toolListImageView.setImageBitmap(item.image)
        binding.toolListItemName.text = item.taskTypeName
        binding.toolListLinearLayout.setOnClickListener { listener.onItemClick(binding.root, item) }
    }
}