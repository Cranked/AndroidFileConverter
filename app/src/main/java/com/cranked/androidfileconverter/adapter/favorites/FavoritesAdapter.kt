package com.cranked.androidfileconverter.adapter.favorites

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
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
                binding.favImage.setImageDrawable(binding.root.context.getDrawable(R.drawable.icon_folder))
            }
            FileType.JPG.type,
            FileType.PNG.type,
            -> {
                Glide.with(binding.root.context).load(item.path).placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(
                    RoundedCorners(10))).into(binding.favImage)
            }
            FileType.WORD.type -> Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_doc)
                .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                .into(binding.favImage)
            FileType.PDF.type -> {

                binding.favImage.setImageBitmap(BitmapUtils.getImageOfPdf(binding.root.context, File(item.path), 0))
//                    Glide.with(binding.root.context).load(item.path)
//                        .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
//                        .into(binding.favImage)
            }
            FileType.EXCEL.type -> {
                Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_psd)
                    .placeholder(R.drawable.custom_dialog).apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(binding.favImage)
            }
            FileType.OTHERS.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_default))
        }
        binding.favItemName.text = item.fileName
    }
}