package com.tracker.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

val networkModule = module {

    // Provide HttpClient
    single {
        HttpClient(CIO) {
            install(WebSockets)
        }
    }
}
