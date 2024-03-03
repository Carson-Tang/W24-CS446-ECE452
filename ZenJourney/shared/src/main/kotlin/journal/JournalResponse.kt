package journal

data class JournalResponse(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val moods: List<String>,
    val content: String,
    val userId: String
)