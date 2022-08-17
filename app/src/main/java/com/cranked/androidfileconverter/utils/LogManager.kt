package com.cranked.androidfileconverter.utils

import android.util.Log

object LogManager {
    fun log(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun log(tag: String, e: Exception) {
        Log.e(tag, e.toString())
    }
}