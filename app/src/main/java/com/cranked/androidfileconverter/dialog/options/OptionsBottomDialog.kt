package com.cranked.androidfileconverter.dialog.options

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.databinding.OptionsBottomFragmentLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class OptionsBottomDialog(
    private val optionsAdapter: OptionsAdapter,
    private val fileName: String,
    private val selectedList: List<TransitionModel>,
) : BaseBottomSheetDialog<OptionsBottomFragmentLayoutBinding>(R.layout.options_bottom_fragment_layout) {
    override fun onBindingCreate(binding: OptionsBottomFragmentLayoutBinding) {

        Glide.with(binding.root.context).load(selectedList.get(0).filePath).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
        binding.optionsRecylerView.adapter = optionsAdapter
        binding.optionsRecylerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.fileNameOptions.text = fileName
    }
}