package com.tracker.stocktracker

import android.app.Application
import com.tracker.network.di.networkModule
import com.tracker.stocktracker.di.domainModule
import com.tracker.stocktracker.di.repositoryModule
import com.tracker.stocktracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StockTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StockTrackerApplication)
            modules(listOf(networkModule, repositoryModule, domainModule, viewModelModule))
        }
    }
}
