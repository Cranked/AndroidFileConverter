package com.cranked.androidfileconverter.adapter

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("viewVisibility")
    @JvmStatic
    fun visibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}