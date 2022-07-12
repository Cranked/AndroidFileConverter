package com.cranked.androidfileconverter.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDatefromTime(dateTime: Long, dateFormat: String): String {
        return SimpleDateFormat(dateFormat).format(Date(dateTime))
    }
}