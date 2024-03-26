package photo

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Photo(
    @BsonId
    val id: ObjectId,
    val userid: ObjectId,
    val photoBase64: String,
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun toResponse() = PhotoResponse(
        id = id.toString(),
        userid = userid.toString(),
        photoBase64 = photoBase64,
        year = year.toString(),
        month = month.toString(),
        day = day.toString()
    )
}