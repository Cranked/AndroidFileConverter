package com.cranked.androidfileconverter.adapter.selectionfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowSelectionFileGridItemBinding
import com.cranked.androidfileconverter.ui.filetype.SelectionFileListener
import com.cranked.androidfileconverter.ui.filetype.SelectionFileModel
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File

class SelectionFileGridAdapter(private val selectionFileListener: SelectionFileListener):
    ListAdapter<SelectionFileModel, SelectionFileGridAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RowSelectionFileGridItemBinding.inflate(
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

    inner class Holder(val binding: RowSelectionFileGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: SelectionFileModel) = binding.run {

            Glide.with(binding.selectionFileGridIV).load(BitmapUtils.getImageByType(binding.root.context, File(item.filePath)))
                .into(binding.selectionFileGridIV)
            binding.selectionFileGridTV.text = item.fileName
            binding.selectionFileGridLinLayout.setBackgroundColor(if (item.isSelected) ContextCompat.getColor(binding.root.context,
                R.color.item_selected_background_color) else ContextCompat.getColor(binding.root.context, R.color.white))

            binding.selectionFileGridLinLayout.setOnClickListener {
                item.isSelected = !item.isSelected
                selectionFileListener.itemSelected(item)
                binding.selectionFileGridLinLayout.setBackgroundColor(if (item.isSelected) binding.root.context.getColor(R.color.item_selected_background_color) else ContextCompat.getColor(
                    binding.root.context,
                    R.color.white))
            }
            binding.selectionFileGridLinLayout.setOnClickListener {
                item.isSelected = !item.isSelected
                selectionFileListener.itemSelected(item)
                if (item.isSelected) {
                    binding.selectionFileGridLinLayout.setBackgroundColor(binding.root.context.getColor(R.color.item_selected_background_color))
                } else {
                    binding.selectionFileGridLinLayout.setBackgroundColor(binding.root.context.getColor(R.color.white))
                }
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