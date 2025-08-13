package com.tracker.stocktracker.domain

import com.tracker.base.model.PriceInfo
import com.tracker.base.domain.PriceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StockTrackerUseCase(private val repo: PriceRepository) {
    val prices: StateFlow<List<PriceInfo>> =
        repo.prices.map { it.values.sortedByDescending { p -> p.price } }
            .stateIn(
                CoroutineScope(Dispatchers.Default + SupervisorJob()),
                started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    val connected = repo.connected

    fun startSocketAndFetchData() = repo.start()
    fun stopSocket() = repo.stop()
}
