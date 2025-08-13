package com.tracker.stocktracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Epoch time to current time
 */
fun epochToDateTime(epochMillis: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = Date(epochMillis)
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}
