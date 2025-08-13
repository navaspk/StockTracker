package com.tracker.stocktracker.di

import com.tracker.base.domain.PriceRepository
import com.tracker.stocktracker.data.StockTrackerWebSocketRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PriceRepository> { StockTrackerWebSocketRepository(get()) }
}
