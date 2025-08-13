package com.tracker.stocktracker.di

import com.tracker.base.domain.BaseStockXRepository
import com.tracker.stocktracker.data.StockTrackerWebSocketRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BaseStockXRepository> { StockTrackerWebSocketRepository(get()) }
}
