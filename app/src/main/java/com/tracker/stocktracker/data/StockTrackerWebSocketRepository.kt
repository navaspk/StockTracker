package com.tracker.stocktracker.data

import com.tracker.base.model.PriceInfo
import com.tracker.base.domain.PriceRepository
import com.tracker.network.NetworkConstants.WEB_SOCKET_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.random.Random

class StockTrackerWebSocketRepository(
    private val client: HttpClient
) : PriceRepository {

    private val _prices = MutableStateFlow<Map<String, PriceInfo>>(emptyMap())
    override val prices = _prices.asStateFlow()

    private val _connected = MutableStateFlow(false)
    override val connected = _connected.asStateFlow()

    private val outgoingSharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 200)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // 25 symbols
    private val symbols = listOf(
        "AAPL", "GOOG", "TSLA", "AMZN", "MSFT", "NVDA", "META", "INTC", "AMD", "ORCL",
        "NFLX", "ADBE", "CRM", "UBER", "LYFT", "BABA", "SPOT", "QCOM", "CSCO", "TXN",
        "SBUX", "NKE", "MCD", "WMT", "DIS"
    )

    private var sessionJob: Job? = null
    private var generatorJob: Job? = null

    init {
        // initialize baseline prices
        val baseline = symbols.associateWith { sym ->
            val price = 50.0 + Random.nextDouble() * 1500.0
            PriceInfo(sym, price, price, System.currentTimeMillis())
        }
        _prices.value = baseline
    }

    override fun start() {
        if (sessionJob != null) return

        sessionJob = scope.launch {
            while (isActive) {
                try {
                    client.webSocket(urlString = WEB_SOCKET_URL) {
                        _connected.value = true

                        val sender = launch {
                            sendMessageToWebSocket(this@webSocket)
                        }

                        val receiver = launch {
                            handleMessageFromSocket(this@webSocket)
                        }

                        joinAll(sender, receiver)
                    }
                } catch (_: Throwable) {
                    _connected.value = false
                    // Wait a bit then reconnect
                    delay(1500)
                } finally {
                    _connected.value = false
                }
            }
        }

        // Generator that emits one update per symbol every 2 seconds
        sendPeriodicMessage()
    }

    private fun sendPeriodicMessage() {
        generatorJob = scope.launch {
            while (isActive) {
                symbols.forEach { sym ->
                    val last = _prices.value[sym]?.price ?: (100.0 + Random.nextDouble() * 200.0)
                    val change = (Random.nextDouble() - 0.5) * (last * 0.01) // +/- ~0.5%
                    val newPrice = (last + change).coerceAtLeast(0.01)
                    val payload = "${sym}|${"%.2f".format(newPrice)}"
                    outgoingSharedFlow.emit(payload) // suspend if buffer full
                }
                delay(2000L)
            }
        }
    }

    private suspend fun sendMessageToWebSocket(webSocketSession: DefaultClientWebSocketSession) {
        outgoingSharedFlow.collect { msg ->
            try {
                webSocketSession.send(Frame.Text(msg))
            } catch (_: Throwable) {
            }
        }
    }

    private suspend fun handleMessageFromSocket(webSocketSession: DefaultClientWebSocketSession) {
        try {
            for (frame in webSocketSession.incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    // Parse "SYMBOL|PRICE"
                    val parts = text.split("|")
                    if (parts.size >= 2) {
                        val sym = parts[0]
                        val price = parts[1].toDoubleOrNull()
                        if (price != null) {
                            _prices.update { current ->
                                val prev = current[sym]?.price ?: price
                                val priceInfoMap = current.toMutableMap()
                                priceInfoMap[sym] = PriceInfo(
                                    sym,
                                    price,
                                    prev,
                                    System.currentTimeMillis()//Instant.now().toEpochMilli()
                                )

                                priceInfoMap
                            }
                        }
                    }
                }
            }
        } catch (_: Throwable) {
            // incoming closed or error — loop will reconnect
        }
    }

    override fun stop() {
        generatorJob?.cancel()
        generatorJob = null
        sessionJob?.cancel()
        sessionJob = null
        _connected.value = false
    }

    override fun close() {
        stop()
        scope.cancel()
    }
}
