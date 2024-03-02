package com.backend.application.request

import com.backend.domain.entity.Photo
import com.backend.domain.entity.User
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class PhotoRequest(
    val userid: String,
    val photoBase64: String,
    val uploadDate: String // in YYYY-MM-DD format
)

fun PhotoRequest.toDomain(): Photo {
    return Photo(
        id = ObjectId(),
        userid = ObjectId(userid),
        photoBase64 = photoBase64,
        uploadDate = uploadDate
    )
}