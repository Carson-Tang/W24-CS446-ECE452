data class StatusResponse(
    val success: Boolean,
    val body: String,
)

fun toStatusResponse(success: Boolean, body: String): StatusResponse {
    return StatusResponse(success, body)
}