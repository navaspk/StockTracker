package com.tracker.base.domain

import com.tracker.base.model.PriceInfo
import kotlinx.coroutines.flow.StateFlow

interface PriceRepository {
    val prices: StateFlow<Map<String, PriceInfo>>
    val connected: StateFlow<Boolean>

    fun start()
    fun stop()
    fun close()
}
