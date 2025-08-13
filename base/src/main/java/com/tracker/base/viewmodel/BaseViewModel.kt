package com.tracker.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.base.ViewEffect
import com.tracker.base.ViewEvent
import com.tracker.base.ViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * BaseViewModel following MVI pattern & will help to provide UIState and Effect back to composable
 * upon user action
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewEffect> :
    ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    private val initialState: UiState by lazy { initialState() }

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<UiState> = _viewState

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _event.collect {
                handleEvent(it)
            }
        }
    }

    fun onEvent(viewEvent: Event) {
        viewModelScope.launch {
            _event.emit(viewEvent)
        }
    }

    fun sendUIState(uiState: UiState.() -> UiState) {
        val newState = _viewState.value.uiState()
        _viewState.value = newState
    }

    /**
     * To manage side effect from compose as viewmodel can send the event to do same like anim, snack bar etc.
     */
    fun sendUIEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    protected fun <T> bindFlowToUIState(
        flow: Flow<T>,
        mapper: UiState.(T) -> UiState
    ) {
        viewModelScope.launch {
            flow.collect { value ->
                sendUIState { mapper(value) }
            }
        }
    }

    abstract fun handleEvent(event: Event)

    abstract fun initialState(): UiState

}