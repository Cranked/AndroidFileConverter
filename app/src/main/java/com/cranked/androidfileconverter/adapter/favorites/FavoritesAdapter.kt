package com.cranked.androidfileconverter.adapter

import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.RowFavoriteAdapterItemBinding
import com.cranked.androidfileconverter.utils.enums.FileType

class FavoritesAdapter(@LayoutRes layoutRes: Int) :
    BaseViewBindingRecyclerViewAdapter<FavoriteFile, RowFavoriteAdapterItemBinding>(layoutRes) {

    override fun setBindingModel(
        item: FavoriteFile,
        binding: RowFavoriteAdapterItemBinding,
        position: Int,
    ) {
        val drawable = R.drawable.icon_folder
        binding.favImage.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                drawable
            )
        )

        when (item.fileType) {
            FileType.FOLDER.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                R.drawable.icon_folder))
            FileType.JPG.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_jpg))
            FileType.PNG.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_png))
            FileType.WORD.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_doc))
            FileType.PDF.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_pdf))
            FileType.OTHERS.type -> binding.favImage.setImageDrawable(binding.root.context.getDrawable(
                com.cranked.androidcorelibrary.R.drawable.icon_default))
        }
        binding.favItemName.text = item.fileName
//        binding.favImage.setImageDrawable(drwable)

    }
}