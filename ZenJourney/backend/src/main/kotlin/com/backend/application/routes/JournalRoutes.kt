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

fun Route.journalRoutes() {
    val journalRepository by inject<JournalRepository>()
    val userRepository by inject<UserRepository>()
    route("/journal") {
        // assumes only 1 journal per date
        post {
            val journal = call.receive<JournalRequest>()
            try {
                val date = LocalDate.of(journal.year, journal.month, journal.day)
                if (date.isAfter(LocalDate.now())) {
                    return@post call.respondText(
                        "The date of the journal cannot be in the future",
                        status = HttpStatusCode.BadRequest
                    )
                }
            } catch (e: DateTimeException) {
                return@post call.respondText(
                    "The year, month, or day provided is invalid",
                    status = HttpStatusCode.BadRequest
                )
            }
            if (journalRepository.findByDate(journal.year, journal.month, journal.day) != null) {
                return@post call.respondText(
                    "A journal already exists for this date",
                    status = HttpStatusCode.BadRequest
                )
            }
            if (userRepository.findById(ObjectId(journal.userId)) == null) {
                return@post call.respondText(
                    "The user provided cannot be found",
                    status = HttpStatusCode.NotFound
                )
            }
            val insertedId = journalRepository.insertOne(journal.toDomain())
            call.respond(
                HttpStatusCode.Created,
                "Created journal with id: ${insertedId.toString()}"
            )
        }
        put("/{id?}") {
            val journalId = call.parameters["id"] ?: return@put call.respondText(
                text = "Missing journal id",
                status = HttpStatusCode.BadRequest
            )
            val updatedJournal = call.receive<JournalRequest>()
            if (journalRepository.updateOne(ObjectId(journalId), updatedJournal) != 1L) {
                return@put call.respondText(
                    "The requested journal could not be updated",
                    status = HttpStatusCode.BadRequest
                )
            }
            call.respond(HttpStatusCode.OK, "Updated journal with id: $journalId")
        }
        delete("/{id?}") {
            val journalId = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing journal id",
                status = HttpStatusCode.BadRequest
            )
            if (journalRepository.deleteById(ObjectId(journalId)) != 1L) {
                return@delete call.respondText(
                    "The requested journal could not be deleted",
                    status = HttpStatusCode.BadRequest
                )
            }
            call.respond(HttpStatusCode.OK, "Deleted journal with id: $journalId")
        }
        get {
            val year = call.request.queryParameters["year"]
            val month = call.request.queryParameters["month"]
            val day = call.request.queryParameters["day"]
            if (year == null || month == null || day == null) {
                return@get call.respondText(
                    "Missing date query parameters (year, month, or day)",
                    status = HttpStatusCode.BadRequest
                )
            }
            val journal = journalRepository.findByDate(year.toInt(), month.toInt(), day.toInt())
                ?: return@get call.respondText(
                    "The requested journal could not be found",
                    status = HttpStatusCode.NotFound
                )
            call.respond(HttpStatusCode.OK, journal.toResponse())
        }
    }
}
