package com.cranked.androidfileconverter.adapter.photo

import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowImageStaggeredItemBinding
import com.cranked.androidfileconverter.ui.model.ImagePreview

class PhotoStaggeredAdapter :
    BaseViewBindingRecyclerViewAdapter<ImagePreview, RowImageStaggeredItemBinding>(R.layout.row_image_staggered_item) {
    override fun setBindingModel(item: ImagePreview, binding: RowImageStaggeredItemBinding, position: Int) {
        binding.imageName.text = item.fileName.substringBeforeLast(".")
        Glide.with(binding.root.context).load(item.path).into(binding.previewImage)
    }
}