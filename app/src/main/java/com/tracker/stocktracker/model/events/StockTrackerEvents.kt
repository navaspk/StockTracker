package com.tracker.stocktracker.model.events

import com.tracker.base.ViewEffect
import com.tracker.base.ViewEvent
import com.tracker.base.ViewState

sealed class StockXEvent : ViewEvent {
    object ToggleStartStop : StockXEvent()
}

data class StockXUiState(
    val isLoading: Boolean = false,
    val toggleStatus: Boolean = false,
    val connectionStatus: Boolean? = null
) : ViewState

class StockXEffect : ViewEffect
