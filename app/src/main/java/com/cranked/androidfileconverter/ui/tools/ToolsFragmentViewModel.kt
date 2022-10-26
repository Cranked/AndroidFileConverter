package com.cranked.androidfileconverter.ui.tools

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.utils.enums.ToolTaskType
import javax.inject.Inject

class ToolsFragmentViewModel @Inject constructor(val context: Context) : BaseViewModel() {

    fun getPdfConverterItems(): ArrayList<ToolModel> {
        val pdfConverterEnumList = arrayListOf<ToolTaskType>(ToolTaskType.PDFTOIMAGES, ToolTaskType.PDFTOEXCEL, ToolTaskType.PDFTOWORD)
        val list = arrayListOf<ToolModel>()
        val drawableList = context.resources.obtainTypedArray(R.array.pdf_converter_tools)
        context.resources.getStringArray(R.array.pdf_converter_strings).forEachIndexed { index, s ->
            list.add(ToolModel(s, drawableList.getDrawable(index)!!.toBitmap(), pdfConverterEnumList[index]))
        }
        return list
    }

}