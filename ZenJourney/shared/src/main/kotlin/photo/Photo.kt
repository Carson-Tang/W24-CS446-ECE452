package photo

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Photo(
    @BsonId
    val id: ObjectId,
    val userid: ObjectId,
    val photoBase64: String,
    val uploadDate: String  // in format YYYY-MM-DD
) {
    fun toResponse() = PhotoResponse(
        id = id.toString(),
        userid = userid.toString(),
        photoBase64 = photoBase64,
        uploadDate = uploadDate
    )
}