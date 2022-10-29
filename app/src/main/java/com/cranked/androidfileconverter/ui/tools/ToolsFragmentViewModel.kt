package com.cranked.androidfileconverter.ui.tools

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.tool.ToolGridAdapter
import com.cranked.androidfileconverter.adapter.tool.ToolListAdapter
import com.cranked.androidfileconverter.databinding.FragmentToolsBinding
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.enums.ToolTaskType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ToolsFragmentViewModel @Inject constructor(val context: Context) : BaseViewModel() {
    private val _changesItems = MutableStateFlow(Boolean)
    val changesItems = _changesItems.asStateFlow()
    fun getPdfConverterItems(activity: FragmentActivity): ArrayList<ToolModel> {
        val pdfConverterEnumList =
            arrayListOf<ToolTaskType>(ToolTaskType.PDFTOIMAGES, ToolTaskType.PDFTOEXCEL, ToolTaskType.PDFTOWORD, ToolTaskType.IMAGETOPDF)
        val list = arrayListOf<ToolModel>()
        val drawableList = context.resources.obtainTypedArray(R.array.pdf_converter_tools)
        activity.resources.getStringArray(R.array.pdf_converter_strings).forEachIndexed { index, s ->
            list.add(ToolModel(s, drawableList.getDrawable(index)!!.toBitmap(), pdfConverterEnumList[index]))
        }
        return list
    }

    fun getPdfToolItems(activity: FragmentActivity): ArrayList<ToolModel> {
        val pdfToolsEnumList = arrayListOf(ToolTaskType.COMPRESSPDF,
            ToolTaskType.SPLITPDF,
            ToolTaskType.MERGEPDF,
            ToolTaskType.LOCKPDF,
            ToolTaskType.UNLOCKPDF,
            ToolTaskType.ROTATEPDF)
        val list = arrayListOf<ToolModel>()
        val drawableList = context.resources.obtainTypedArray(R.array.pdf_tools_images)
        activity.resources.getStringArray(R.array.pdf_tools_strings).forEachIndexed { index, s ->
            list.add(ToolModel(s, drawableList.getDrawable(index)!!.toBitmap(), pdfToolsEnumList[index]))
        }

        return list
    }

    fun setAdapter(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager,
        adapter: ToolGridAdapter,
        items: ArrayList<ToolModel>,
    ): ToolGridAdapter {
        adapter.setItems(items)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return adapter
    }

    fun setAdapter(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager,
        adapter: ToolListAdapter,
        items: ArrayList<ToolModel>,
    ): ToolListAdapter {
        adapter.setItems(items)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return adapter
    }

    fun init(
        app: FileConvertApp,
        context: Context,
        binding: FragmentToolsBinding,
        converterListAdapter: ToolListAdapter,
        converterGridAdapter: ToolGridAdapter,
        toolListAdapter: ToolListAdapter,
        toolGridAdapter: ToolGridAdapter,
        pdfConvertersList: ArrayList<ToolModel>,
        pdfToolsList: ArrayList<ToolModel>,
    ) {
        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> {
                setAdapter(binding.pdfConvertersRV,
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false),
                    converterListAdapter,
                    pdfConvertersList)
                setAdapter(binding.pdfToolRV,
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false),
                    toolListAdapter,
                    pdfToolsList)


            }
            LayoutState.GRID_LAYOUT.value -> {
                setAdapter(binding.pdfConvertersRV,
                    GridLayoutManager(context, 3),
                    converterGridAdapter,
                    pdfConvertersList)
                setAdapter(binding.pdfToolRV, GridLayoutManager(context, 3), toolGridAdapter, pdfToolsList)

            }
        }
    }
}