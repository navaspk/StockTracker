package com.tracker.stocktracker

import com.tracker.stocktracker.domain.StockTrackerUseCase
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.ui.viewmodel.StockTrackerViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockTrackerViewModelTest {

    private lateinit var viewModel: StockTrackerViewModel
    private lateinit var useCase: StockTrackerUseCase

    private val connectedFlow = MutableStateFlow(false)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock use case
        useCase = mockk(relaxed = true) {
            every { connected } returns connectedFlow
        }

        viewModel = StockTrackerViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update connectionStatus when connected emits`() = runTest {
        connectedFlow.emit(true)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.viewState.value
        assertEquals(true, state.connectionStatus)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `should start socket when toggle is true`() = runTest {
        viewModel.onEvent(StockXEvent.ToggleStartStop)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { useCase.startSocketAndFetchData() }
    }

    @Test
    fun `should stop socket when toggle is false`() = runTest {
        // First toggle to true
        viewModel.onEvent(StockXEvent.ToggleStartStop)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then toggle back to false
        viewModel.onEvent(StockXEvent.ToggleStartStop)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { useCase.stopSocket() }
    }
}