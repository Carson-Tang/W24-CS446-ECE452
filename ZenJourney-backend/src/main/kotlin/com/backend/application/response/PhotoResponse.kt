package com.backend.application.response

import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
        val id: String,
        val userid: String,
        val photoBase64: String,
        val uploadDate: String  // in format YYYY-MM-DD
)