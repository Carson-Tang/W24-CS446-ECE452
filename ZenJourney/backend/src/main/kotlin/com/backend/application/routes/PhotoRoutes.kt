package com.backend.application.routes

import com.backend.domain.ports.PhotoRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import photo.ListResponse
import photo.PhotoRequest
import photo.toDomain

fun Route.photoRoutes() {
    val repository by inject<PhotoRepository>()
    //insertPhoto(photo: Photo)        /photo
    route("/photo") {
        post {
            val photo = call.receive<PhotoRequest>()
            val insertedId = repository.insertPhoto(photo.toDomain())
            if (insertedId == null){
                call.respond(HttpStatusCode.InternalServerError, "Failed to create photo")
            }else{
                call.respond(HttpStatusCode.Created, mapOf("id" to "$insertedId"))
            }
        }

        //deleteById(id: ObjectId)             /photo/{id}
        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing photo id", status = HttpStatusCode.BadRequest
            )
            val delete: Long = repository.deleteById(ObjectId(id))
            if (delete == 1L) {
                return@delete call.respondText(
                    "Photo Deleted successfully", status = HttpStatusCode.OK
                )
            }
            return@delete call.respondText("Photo not found", status = HttpStatusCode.NotFound)
        }

        //deleteByUserId(userid: ObjectId)      /photo/byuserid/{userid}
        delete("/byuserid/{userid?}") {
            val id = call.parameters["userid"] ?: return@delete call.respondText(
                text = "Missing user id in photo deletion", status = HttpStatusCode.BadRequest
            )
            val delete: Long = repository.deleteByUserId(ObjectId(id))
            if (delete != -1L) {
                return@delete call.respondText(
                    "Photo(s) from User Deleted successfully", status = HttpStatusCode.OK
                )
            }
            return@delete call.respondText(
                "User for photo deletion not found", status = HttpStatusCode.NotFound
            )
        }

        //findById(id: ObjectId):          /photo/{id}
        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing photo id", status = HttpStatusCode.BadRequest
                )
            }
            repository.findById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respondText("No records found for photo id $id")
        }

        // findByUserId(userid: ObjectId)  /photo/byuserid/{userid}
        // if month and year are not null, then 
        // findByYearMonth(userid: ObjectId, year: Int, month: Int)
        get("/byuserid/{userid?}") {
            val id = call.parameters["userid"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing user id in photo GET", status = HttpStatusCode.BadRequest
                )
            }
            val year = call.request.queryParameters["year"]
            val month = call.request.queryParameters["month"]
            val list = if (month != null && year != null){
                repository.findByYearMonth(ObjectId(id), year.toInt(), month.toInt())
            }else{
                repository.findByUserId(ObjectId(id))
            }
            val photoresponse = list.map {
                it.toResponse()
            }
            call.respond(ListResponse(photoresponse))
        }

    }
}