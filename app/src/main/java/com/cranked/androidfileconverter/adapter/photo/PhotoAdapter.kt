package com.cranked.androidfileconverter.adapter.photo

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowTakenPhotoBinding

class PhotoAdapter : BaseViewBindingRecyclerViewAdapter<PhotoFile, RowTakenPhotoBinding>(R.layout.row_taken_photo) {
    override fun setBindingModel(item: PhotoFile, binding: RowTakenPhotoBinding, position: Int) {
        binding.takenPhotoFileName.text = item.fileName
        binding.elementsCountTextView.text = binding.root.context.getString(R.string.item_counts, item.itemCount)
        binding.dateTextView.text = item.date
        val list = FileUtils.getFolderFiles(item.path, 10000, 1).filter { it.isFile }
        val imagePath = if (list.size > 0) list[0].path else ""
        Glide.with(binding.root.context).load(imagePath).apply(RequestOptions().transform(RoundedCorners(10)))
            .placeholder(R.drawable.icon_broken_image)
            .into(binding.takenPhotoImageView)
    }
}