package photo

import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
        val id: String,
        val userid: String,
        val photoBase64: String,
        val year: String,
        val month: String,
        val day: String
)