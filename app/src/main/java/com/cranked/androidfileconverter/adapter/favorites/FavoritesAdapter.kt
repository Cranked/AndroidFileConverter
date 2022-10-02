package com.cranked.androidfileconverter.adapter.favorites

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.RowFavoriteAdapterItemBinding
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class FavoritesAdapter(@LayoutRes layoutRes: Int) :
    BaseViewBindingRecyclerViewAdapter<FavoriteFile, RowFavoriteAdapterItemBinding>(layoutRes) {
    override fun setBindingModel(
        item: FavoriteFile,
        binding: RowFavoriteAdapterItemBinding,
        position: Int,
    ) {
        when (item.fileType) {
            FileType.FOLDER.type -> {
                binding.favImage.visibility = View.VISIBLE
                Glide.with(binding.favImage).load(R.drawable.icon_folder).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.IMMEDIATE).into(binding.favImage)
            }
            FileType.JPG.type,
            FileType.PNG.type,
            -> {
                Glide.with(binding.root.context).load(item.path).placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(
                    RoundedCorners(10))).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE).into(binding.favImage)
            }
            FileType.WORD.type -> Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_doc)
                .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                .diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                .into(binding.favImage)
            FileType.PDF.type -> {
                val bitmap = BitmapUtils.getImagePdf(File(item.path))
                Glide.with(binding.root.context).load(bitmap).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.favImage)
            }
            FileType.EXCEL.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_psd)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                    .into(binding.favImage)
            }
            FileType.OTHERS.type -> Glide.with(binding.favImage).load(com.cranked.androidcorelibrary.R.drawable.icon_default)
                .into(binding.favImage)

        }
        binding.favItemName.text = item.fileName
    }
}