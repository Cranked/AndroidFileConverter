package com.cranked.androidfileconverter.adapter.selectionfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cranked.androidfileconverter.databinding.RowSelectedListItemBinding
import com.cranked.androidfileconverter.ui.filetype.SelectionFileListener
import com.cranked.androidfileconverter.ui.filetype.SelectionFileModel
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class SelectedFileAdapter(private val selectionFileListener: SelectionFileListener) :
    ListAdapter<SelectionFileModel, SelectedFileAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RowSelectedListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class Holder(val binding: RowSelectedListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: SelectionFileModel) = binding.run {
            Glide.with(binding.rowSelectedItemListIV).load(BitmapUtils.getImageByType(binding.root.context, File(item.filePath)))
                .into(binding.rowSelectedItemListIV)
            binding.rowSelectedItemListTV.text = item.fileName
            binding.removeSelectedItem.setOnClickListener {
                selectionFileListener.itemSelected(item)
            }
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<SelectionFileModel>() {
        override fun areItemsTheSame(oldItem: SelectionFileModel, newItem: SelectionFileModel): Boolean =
            oldItem.filePath == newItem.filePath

        override fun areContentsTheSame(oldItem: SelectionFileModel, newItem: SelectionFileModel): Boolean =
            oldItem == newItem

    }
}