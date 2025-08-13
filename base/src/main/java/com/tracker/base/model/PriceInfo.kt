package com.tracker.base.model

/**
 * Model class for storing stock info
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
data class PriceInfo(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
)