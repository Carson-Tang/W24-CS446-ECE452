package com.backend.application.routes

import com.backend.domain.ports.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import toStatusResponse
import user.UserRequest
import user.toDomain
import org.mindrot.jbcrypt.BCrypt
import com.backend.jwtConfig.JwtConfig

fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()
    route("/user") {
        post("/login") {
            val user = call.receive<UserRequest>()
            val existingUser = userRepository.findByEmail(user.email)
            val token = JwtConfig.generateToken(user)
            if (existingUser != null) {
                if (BCrypt.checkpw(user.password, existingUser.password)) {
                    return@post call.respond(
                        HttpStatusCode.OK,
                        hashMapOf("token" to token),
                    )
                } else {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        toStatusResponse(false, "Your email and password do not match. Please try again.")
                    )
                }
            }
            return@post call.respond(
                HttpStatusCode.BadRequest,
                toStatusResponse(false, "Your email and password do not match. Please try again.")
            )
        }

        post {
            val user = call.receive<UserRequest>()
            val token = JwtConfig.generateToken(user)
            val existingUser = userRepository.findByEmail(user.email)
            if (existingUser != null) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "User with email ${user.email} already exists")
                )
            }

            val insertedId = userRepository.insertOne(user.toDomain())

            if (insertedId != null) {
                return@post call.respond(
                    HttpStatusCode.Created,
                    hashMapOf("token" to token),
                )
            }
        }

        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                toStatusResponse(false, "Missing user id")
            )
            val delete: Long = userRepository.deleteById(ObjectId(id))
            if (delete == 1L) {
                return@delete call.respond(
                    HttpStatusCode.OK,
                    toStatusResponse(true, "User deleted successfully")
                )
            }
            return@delete call.respond(
                HttpStatusCode.NotFound,
                toStatusResponse(false, "User not found")
            )
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Missing id")
                )
            }
            userRepository.findById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respond(
                HttpStatusCode.NotFound,
                toStatusResponse(false, "No records found for id: $id")
            )
        }
    }
}