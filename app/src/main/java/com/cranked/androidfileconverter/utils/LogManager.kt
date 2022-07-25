package com.cranked.androidfileconverter.utils

import android.util.Log

object LogManager {
    fun log(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}