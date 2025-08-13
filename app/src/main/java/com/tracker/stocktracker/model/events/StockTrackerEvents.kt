package com.tracker.stocktracker.model.events

import com.tracker.base.ViewEffect
import com.tracker.base.ViewEvent
import com.tracker.base.ViewState

/**
 * Event class to set up event, state and effects
 * Effects is like SideEffect which helps to make some operation which is out of composable
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
sealed class StockXEvent : ViewEvent {
    object ToggleStartStop : StockXEvent()
}

data class StockXUiState(
    val isLoading: Boolean = false,
    val toggleStatus: Boolean = false,
    val connectionStatus: Boolean? = null
) : ViewState

class StockXEffect : ViewEffect
