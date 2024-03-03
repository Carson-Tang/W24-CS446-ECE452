package journal

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Journal(
    @BsonId
    val id: ObjectId,
    val year: Int,
    val month: Int,
    val day: Int,
    val moods: List<String>,
    val content: String,
    val userId: ObjectId
) {
    fun toResponse() = JournalResponse(
        id = id.toString(),
        year = year,
        month = month,
        day = day,
        moods = moods,
        content = content,
        userId = userId.toString()
    )
}