package journal

import org.bson.types.ObjectId

data class JournalRequest(
    val year: Int,
    val month: Int,
    val day: Int,
    val moods: List<String>,
    val content: String,
    val userId: String,
)

fun JournalRequest.toDomain(): Journal {
    return Journal(
        id = ObjectId(),
        year = year,
        month = month,
        day = day,
        moods = moods,
        content = content,
        userId = ObjectId(userId),
    )
}