package com.backend.plugins

import com.backend.application.routes.photoRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.backend.application.routes.userRoutes

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoutes()
        photoRoutes()
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static") {
            resources("static")
        }
    }
}
