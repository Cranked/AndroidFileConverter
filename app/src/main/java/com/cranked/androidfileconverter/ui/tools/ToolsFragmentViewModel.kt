package com.cranked.androidfileconverter.ui.tools

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.tool.ToolGridAdapter
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

    fun getPdfToolItems(): ArrayList<ToolModel> {
        val pdfToolsEnumList = arrayListOf(ToolTaskType.COMPRESSPDF,
            ToolTaskType.SPLITPDF,
            ToolTaskType.MERGEPDF,
            ToolTaskType.LOCKPDF,
            ToolTaskType.UNLOCKPDF,
            ToolTaskType.ROTATEPDF)
        val list = arrayListOf<ToolModel>()
        val drawableList = context.resources.obtainTypedArray(R.array.pdf_tools_images)
        context.resources.getStringArray(R.array.pdf_tools_strings).forEachIndexed { index, s ->
            list.add(ToolModel(s, drawableList.getDrawable(index)!!.toBitmap(), pdfToolsEnumList[index]))
        }

        return list
    }

    suspend fun setAdapter(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager,
        adapter: ToolGridAdapter,
        items: ArrayList<ToolModel>,
    ) {
        recyclerView.layoutManager = layoutManager
        adapter.setItems(items)
        recyclerView.adapter = adapter
    }
}