package com.cranked.androidfileconverter.dialog.favorite

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.databinding.OptionsBottomFragmentLayoutBinding

class FavoriteOptionsBottomDialog(private val optionsAdapter: OptionsAdapter) :
    BaseBottomSheetDialog<OptionsBottomFragmentLayoutBinding>(R.layout.options_bottom_fragment_layout) {
    override fun onBindingCreate(binding: OptionsBottomFragmentLayoutBinding) {
        binding.optionsBottomLinearLayout.visibility = View.GONE
        binding.seperateLayout.visibility = View.GONE
        binding.optionsRecylerView.adapter = optionsAdapter
        binding.optionsRecylerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
    }
}