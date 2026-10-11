package com.maxdgf.newspaper.network

import android.util.Log
import com.maxdgf.newspaper.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val KTOR_LOG_TAG = "* [Ktor]"

private const val KTOR_CLIENT_CONNECT_TIMEOUT = 20_000L
private const val KTOR_CLIENT_REQUEST_TIMEOUT = 15_000L
private const val KTOR_CLIENT_SOCKET_TIMEOUT = 10_000L

val httpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                useAlternativeNames = true
                ignoreUnknownKeys = true
                encodeDefaults = false
            }
        )
    }

    install(HttpTimeout) {
        connectTimeoutMillis = KTOR_CLIENT_CONNECT_TIMEOUT
        requestTimeoutMillis = KTOR_CLIENT_REQUEST_TIMEOUT
        socketTimeoutMillis = KTOR_CLIENT_SOCKET_TIMEOUT
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v(KTOR_LOG_TAG, message)
            }
        }
        level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
    }
}