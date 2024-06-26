package com.backend.application.routes

import com.backend.domain.ports.JournalRepository
import com.backend.domain.ports.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import journal.JournalRequest
import journal.toDomain
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import java.time.DateTimeException
import java.time.LocalDate
import toStatusResponse

fun Route.journalRoutes() {
    val journalRepository by inject<JournalRepository>()
    val userRepository by inject<UserRepository>()
    route("/journal") {
        post {
            val journal = call.receive<JournalRequest>()
            try {
                val date = LocalDate.of(journal.year, journal.month, journal.day)
                if (date.isAfter(LocalDate.now())) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        toStatusResponse(false, "The date of the journal cannot be in the future")
                    )
                }
            } catch (e: DateTimeException) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "The year, month, or day provided is invalid")
                )
            }
            if (userRepository.findById(ObjectId(journal.userId)) == null) {
                return@post call.respond(
                    HttpStatusCode.NotFound,
                    toStatusResponse(false, "The user provided cannot be found")
                )
            }
            if (journalRepository.findByDateAndUser(journal.userId, journal.year, journal.month, journal.day) != null) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "A journal already exists for this date and user")
                )
            }
            val insertedId = journalRepository.insertOne(journal.toDomain())
            if (insertedId != null) {
                call.respond(
                    HttpStatusCode.Created,
                    toStatusResponse(true, "Created journal with id: ${insertedId.asObjectId().value}")
                )
            }
        }
        put("/{id?}") {
            val journalId = call.parameters["id"] ?: return@put call.respondText(
                text = "Missing journal id",
                status = HttpStatusCode.BadRequest
            )
            val updatedJournal = call.receive<JournalRequest>()
            if (journalRepository.updateOne(ObjectId(journalId), updatedJournal) != 1L) {
                return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    toStatusResponse(false, "The requested journal could not be updated")
                )
            }
            call.respond(
                HttpStatusCode.OK,
                toStatusResponse(true, "Updated journal with id: $journalId")
            )
        }
        delete("/{id?}") {
            val journalId = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                toStatusResponse(false, "Missing journal id")
            )
            if (journalRepository.deleteById(ObjectId(journalId)) != 1L) {
                return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "The requested journal could not be deleted")
                )
            }
            call.respond(
                HttpStatusCode.OK,
                toStatusResponse(true, "Deleted journal with id: $journalId")
            )
        }
        get {
            val year = call.request.queryParameters["year"]
            val month = call.request.queryParameters["month"]
            val day = call.request.queryParameters["day"]
            if (year == null || month == null || day == null) {
                return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Missing date query parameters (year, month, or day)")
                )
            }
            val journal = journalRepository.findByDate(year.toInt(), month.toInt(), day.toInt())
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    toStatusResponse(false, "The requested journal could not be found")
                )
            call.respond(HttpStatusCode.OK, journal.toResponse())
        }
    }
    route("/journal/user") {
        get {
            val userId = call.request.queryParameters["user"]
            val year = call.request.queryParameters["year"]
            val month = call.request.queryParameters["month"]
            val day = call.request.queryParameters["day"]
            if (userId == null || year == null || month == null || day == null) {
                return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Missing date query parameters (user, year, month, or day)")
                )
            }
            val journal = journalRepository.findByDateAndUser(userId, year.toInt(), month.toInt(), day.toInt())
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    toStatusResponse(false, "The requested journal could not be found")
                )
            call.respond(HttpStatusCode.OK, journal.toResponse())
        }
        get("/month") {
            val userId = call.request.queryParameters["user"]
            val year = call.request.queryParameters["year"]
            val month = call.request.queryParameters["month"]

            if (userId == null || year == null || month == null) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Missing query parameters: user, year and/or month are required")
                )
            }

            val yearInt = year.toIntOrNull()
            val monthInt = month.toIntOrNull()
            if (yearInt == null || monthInt == null || monthInt !in 1..12) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Invalid year or month. Ensure year is a number and month is between 1 and 12.")
                )
            }

            try {
                // Fetch journals for the specified month and year
                val journals = journalRepository.findByMonth(userId = userId, year = yearInt, month = monthInt)
                if (journals.isEmpty()) {
                    return@get call.respond(
                        HttpStatusCode.NotFound,
                        toStatusResponse(false, "No journals found for the specified month and year")
                    )
                }
                // Respond with the list of journals
                call.respond(HttpStatusCode.OK, journals.map { it.toResponse() })
            } catch (e: Exception) {
                // Handle potential errors, such as issues with the database
                call.respond(
                    HttpStatusCode.InternalServerError,
                    toStatusResponse(false, "An error occurred: ${e.message}")
                )
            }
        }
        delete {
            val userId = call.request.queryParameters["userId"]
                ?: return@delete call.respond(
                    status = HttpStatusCode.BadRequest,
                    toStatusResponse(false, "Missing user ID query parameter")
                )

            try {
                journalRepository.deleteByUserId(userId)
                call.respond(HttpStatusCode.OK, toStatusResponse(true, "Journals deleted successfully"))
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    toStatusResponse(false, "Failed to delete journals: ${e.message}")
                )
            }
        }
    }
}
