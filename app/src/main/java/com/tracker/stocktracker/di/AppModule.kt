@file:Suppress("DEPRECATION")

package com.tracker.stocktracker.di

import com.tracker.base.domain.PriceRepository
import com.tracker.stocktracker.domain.StockTrackerUseCase
import com.tracker.stocktracker.ui.viewmodel.StockTrackerViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val domainModule = module {
    single { StockTrackerUseCase(get() as PriceRepository) }
}

val viewModelModule = module {
    viewModel { StockTrackerViewModel(get()) }
}