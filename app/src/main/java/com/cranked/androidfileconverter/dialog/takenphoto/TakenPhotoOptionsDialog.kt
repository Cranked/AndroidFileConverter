package com.cranked.androidfileconverter.dialog.takenphoto

import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.dialog.BaseBottomSheetDialog
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.databinding.TakenPhotoLayoutBinding

class TakenPhotoOptionsDialog(private val optionsAdapter: OptionsAdapter, private val fileName: String) :
    BaseBottomSheetDialog<TakenPhotoLayoutBinding>(R.layout.taken_photo_layout) {
    /*
    ÖNEMLİ
    TAKEN PHOTO OPTIONS DIALOGTA CONSTRAİNT LAYOUT KULLANILMAMALI LİNEAR LAYOUT KULLANILMALI
    */

    override fun onBindingCreate(binding: TakenPhotoLayoutBinding) {
        binding.detailFileName.text = fileName
        binding.takenPhotoRecyclerView.adapter = optionsAdapter
        binding.takenPhotoRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
    }
}