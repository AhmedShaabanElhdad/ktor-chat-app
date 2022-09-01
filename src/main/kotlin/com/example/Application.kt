package com.example

import com.example.di.configureKoin
import com.example.di.mainModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.*
import org.koin.core.Koin
import org.koin.dsl.module

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        configureSockets()
        configureSerialization()
        configureMonitoring()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
