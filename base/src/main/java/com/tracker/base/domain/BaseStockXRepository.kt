package com.tracker.base.domain

import com.tracker.base.model.PriceInfo
import kotlinx.coroutines.flow.StateFlow

/**
 * Base repo helps to communicate to data layer to start / stop websocket
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
interface BaseStockXRepository {
    val prices: StateFlow<Map<String, PriceInfo>>
    val connected: StateFlow<Boolean>

    fun start()
    fun stop()
    fun close()
}
