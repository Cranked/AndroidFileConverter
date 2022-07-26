package com.cranked.androidfileconverter.dialog.options

import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.databinding.OptionsBottomFragmentLayoutBinding

class OptionsBottomDialog(
    private val optionsAdapter: OptionsAdapter,
    private val fileName: String,
) : BaseBottomSheetDialog<OptionsBottomFragmentLayoutBinding>(R.layout.options_bottom_fragment_layout) {
    override fun onBindingCreate(binding: OptionsBottomFragmentLayoutBinding) {

        binding.optionsRecylerView.adapter = optionsAdapter
        binding.optionsRecylerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.fileNameOptions.text = fileName
    }
}