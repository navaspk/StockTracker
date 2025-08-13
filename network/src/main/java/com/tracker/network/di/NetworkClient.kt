package com.tracker.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

/**
 * Di for adding Ktor client for WebSocket
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
val networkModule = module {

    // Provide HttpClient
    single {
        HttpClient(CIO) {
            install(WebSockets)
        }
    }
}
