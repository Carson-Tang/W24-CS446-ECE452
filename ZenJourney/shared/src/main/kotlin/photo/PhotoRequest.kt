package photo

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class PhotoRequest(
    val userid: String,
    val photoBase64: String,
    val year: String,
    val month: String,
    val day: String
)

fun PhotoRequest.toDomain(): Photo {
    return Photo(
        id = ObjectId(),
        userid = ObjectId(userid),
        photoBase64 = photoBase64,
        year = year.toInt(),
        month = month.toInt(),
        day = day.toInt()
    )
}