package com.cranked.androidfileconverter.adapter

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.RowFavoriteAdapterItemBinding
import java.io.File

class FavoritesAdapter :
    BaseViewBindingRecyclerViewAdapter<FavoriteFile, RowFavoriteAdapterItemBinding>(R.layout.row_favorite_adapter_item) {

    override fun setBindingModel(
        item: FavoriteFile,
        binding: RowFavoriteAdapterItemBinding,
        position: Int
    ) {
        val drawable = R.drawable.icon_folder
        binding.favImage.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                drawable
            )
        )
        binding.favItemName.text = getItems()[position].fileName
        val item = File(getItems()[position].fileName)
        var drwable: Drawable?
        if (item.isDirectory) {
            drwable = ContextCompat.getDrawable(binding.root.context, R.drawable.icon_folder)
        } else {
            drwable = ContextCompat.getDrawable(
                binding.root.context,
               R.drawable.icon_folder
            )
        }
        binding.favImage.setImageDrawable(drwable)

    }
}