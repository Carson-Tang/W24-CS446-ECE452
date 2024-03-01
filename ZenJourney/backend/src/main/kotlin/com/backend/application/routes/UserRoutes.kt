package com.backend.application.routes

import com.backend.domain.ports.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.route
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import user.UserRequest
import user.toDomain

fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()
    route("/user") {
        post {
            val user = call.receive<UserRequest>()
            val insertedId = userRepository.insertOne(user.toDomain())
            call.respond(HttpStatusCode.Created, "Created user with id: $insertedId")
        }

        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                    text = "Missing user id",
                    status = HttpStatusCode.BadRequest
            )
            val delete: Long = userRepository.deleteById(ObjectId(id))
            if (delete == 1L) {
                return@delete call.respondText("User deleted successfully", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("User not found", status = HttpStatusCode.NotFound)
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                        text = "Missing id",
                        status = HttpStatusCode.BadRequest
                )
            }
            userRepository.findById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respondText("No records found for id: $id")
        }
    }
}