package com.tracker.stocktracker.ui.viewmodel

import com.tracker.base.viewmodel.BaseViewModel
import com.tracker.stocktracker.domain.StockTrackerUseCase
import com.tracker.stocktracker.model.events.StockXEffect
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.model.events.StockXUiState

class StockTrackerViewModel(private val useCase: StockTrackerUseCase) :
    BaseViewModel<StockXEvent, StockXUiState, StockXEffect>() {

    val prices = useCase.prices

    init {
        bindFlowToUIState(useCase.connected) { status ->
            copy(
                connectionStatus = status,
                isLoading = false
            )
        }
    }

    override fun handleEvent(event: StockXEvent) {
        when (event) {
            StockXEvent.ToggleStartStop -> performToggleStartStop()
        }
    }

    override fun initialState(): StockXUiState = StockXUiState(isLoading = true)

    private fun performToggleStartStop() {
        val newToggle = !viewState.value.toggleStatus

        sendUIState {
            copy(toggleStatus = newToggle, isLoading = false)
        }

        if (newToggle) useCase.startSocketAndFetchData() else useCase.stopSocket()
    }

    override fun onCleared() {
        useCase.stopSocket()
        super.onCleared()
    }
}
