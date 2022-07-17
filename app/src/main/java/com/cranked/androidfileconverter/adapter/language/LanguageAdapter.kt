package com.cranked.androidfileconverter.adapter.language

import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.RowLanguageItemBinding
import com.cranked.androidfileconverter.ui.languages.LanguageModel

class LanguageAdapter :
    BaseViewBindingRecyclerViewAdapter<LanguageModel, RowLanguageItemBinding>(R.layout.row_language_item) {
    override fun setBindingModel(
        item: LanguageModel,
        binding: RowLanguageItemBinding,
        position: Int,
    ) {
        binding.languageTextView.text = item.name
        binding.languageRadioButton.isChecked = item.checkedState
    }
}