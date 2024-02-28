package com.backend

import com.backend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*

fun main() {
    embeddedServer(Jetty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
