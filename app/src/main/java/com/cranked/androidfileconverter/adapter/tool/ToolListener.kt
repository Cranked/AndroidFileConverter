package com.cranked.androidfileconverter.adapter.tool

import android.view.View
import com.cranked.androidfileconverter.ui.tools.ToolModel

interface ToolListener {
    fun onItemClick(view: View, item: ToolModel)
}