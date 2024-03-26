package com.backend.plugins

import com.backend.application.routes.photoRoutes
import com.backend.application.routes.journalRoutes
import com.backend.application.routes.userProtectedRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.backend.application.routes.userRoutes
import io.ktor.server.auth.authenticate

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoutes()
        authenticate("auth-jwt") {
            userProtectedRoutes()
            photoRoutes()
            journalRoutes()
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
