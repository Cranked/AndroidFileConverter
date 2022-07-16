package com.cranked.androidfileconverter.utils.date

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDatefromTime(dateTime: Long, dateFormat: String): String {
        return SimpleDateFormat(dateFormat).format(Date(dateTime))
    }
}