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
import io.ktor.server.response.respondText
import io.ktor.server.routing.put

fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()
    route("/user") {
        post("/login") {
            val user = call.receive<UserRequest>()
            val existingUser = userRepository.findByEmail(user.email)
            if (existingUser != null) {
                val token = JwtConfig.generateToken(user, existingUser.id.toString())
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
            val existingUser = userRepository.findByEmail(user.email)
            if (existingUser != null) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "User with email ${user.email} already exists")
                )
            }

            val insertedId = userRepository.insertOne(user.toDomain())

            if (insertedId != null) {
                val userId = insertedId.asObjectId().value.toString()
                val token = JwtConfig.generateToken(user, userId)
                return@post call.respond(
                    HttpStatusCode.Created,
                    hashMapOf("token" to token),
                )
            }
        }

        put("/{id?}") {
            val userId = call.parameters["id"] ?: return@put call.respondText(
                text = "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val updatedUser = call.receive<UserRequest>()
            if (userRepository.updateOne(ObjectId(userId), updatedUser) != 1L) {
                return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    toStatusResponse(false, "The requested user could not be updated")
                )
            }
            call.respond(
                HttpStatusCode.OK,
                toStatusResponse(true, "Updated user with id: $userId")
            )
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

fun Route.userProtectedRoutes() {
    val userRepository by inject<UserRepository>()
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
}