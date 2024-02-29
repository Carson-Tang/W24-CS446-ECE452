package com.backend.domain.entity

import com.backend.application.response.UserResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id: ObjectId,
    val name: String,
    val email: String,
    val password: String
) {
    fun toResponse() = UserResponse(
        id = id.toString(),
        name = name,
        email = email,
        password = password
    )
}