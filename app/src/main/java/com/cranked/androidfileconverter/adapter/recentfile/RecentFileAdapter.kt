package com.cranked.androidfileconverter.adapter.recentfile

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.entity.RecentFile
import com.cranked.androidfileconverter.databinding.RowRecentfileItemBinding
import java.io.File

class RecentFileAdapter :
    BaseViewBindingRecyclerViewAdapter<RecentFile, RowRecentfileItemBinding>(R.layout.row_recentfile_item) {
    override fun setBindingModel(
        recentFile: RecentFile,
        binding: RowRecentfileItemBinding,
        position: Int
    ) {
        val drawable = R.drawable.icon_folder
        binding.recentFileImage.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                drawable
            )
        )
        binding.recentFileName.text = recentFile.fileName
        val item = File(recentFile.fileName)
        var drwable: Drawable?
        if (item.isDirectory) {
            drwable = ContextCompat.getDrawable(binding.root.context, R.drawable.icon_folder)
        } else {
            drwable = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.icon_folder
            )
        }
        binding.recentFileImage.setImageDrawable(drwable)

    }
}