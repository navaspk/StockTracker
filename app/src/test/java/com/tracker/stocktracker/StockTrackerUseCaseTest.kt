package com.tracker.stocktracker

import com.tracker.base.domain.BaseStockXRepository
import com.tracker.base.model.PriceInfo
import com.tracker.stocktracker.domain.StockTrackerUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockTrackerUseCaseTest {

    private lateinit var repo: BaseStockXRepository
    private lateinit var useCase: StockTrackerUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val pricesFlow =
        MutableStateFlow<Map<String, PriceInfo>>(emptyMap())
    private val connectedFlow = MutableStateFlow(false)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repo = mockk(relaxed = true)
        every { repo.prices } returns pricesFlow
        every { repo.connected } returns connectedFlow

        useCase = StockTrackerUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startSocketAndFetchData calls repo start`() {
        useCase.startSocketAndFetchData()
        verify { repo.start() }
    }

    @Test
    fun `stopSocket calls repo stop`() {
        useCase.stopSocket()
        verify { repo.stop() }
    }
}
