package com.cranked.androidfileconverter.dialog.options

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.databinding.OptionsBottomFragmentLayoutBinding
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.enums.FileType

class OptionsBottomDialog(
    private val optionsAdapter: OptionsAdapter,
    private val fileName: String,
    private val selectedList: List<TransitionModel>,
) : BaseBottomSheetDialog<OptionsBottomFragmentLayoutBinding>(R.layout.options_bottom_fragment_layout) {
    override fun onBindingCreate(binding: OptionsBottomFragmentLayoutBinding) {
         when(selectedList.get(0).fileType) {
         FileType.FOLDER.type-> Glide.with(binding.root.context).load(binding.root.context.getDrawable(R.drawable.icon_folder)).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
             FileType.JPG.type,FileType.PNG.type-> Glide.with(binding.root.context).load(selectedList.get(0).filePath).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
             FileType.PDF.type-> Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_pdf).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
             FileType.EXCEL.type-> Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_raw).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
             FileType.WORD.type-> Glide.with(binding.root.context).load(com.cranked.androidcorelibrary.R.drawable.icon_doc).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
             else-> Glide.with(binding.root.context).load(selectedList.get(0).filePath).placeholder(R.drawable.custom_dialog).into(binding.optionBottomImageView)
         }
        binding.optionsRecylerView.adapter = optionsAdapter
        binding.optionsRecylerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.fileNameOptions.text = fileName
    }
}