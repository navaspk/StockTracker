package com.tracker.base.model

data class PriceInfo(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
)