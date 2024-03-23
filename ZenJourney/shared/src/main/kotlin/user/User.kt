package user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id: ObjectId,
    val name: String,
    val email: String,
    val password: String,
    val pin: String,
    val useJournalForAffirmations: Boolean,
) {
    fun toResponse() = UserResponse(
        id = id.toString(),
        name = name,
        email = email,
        password = password,
        pin = pin,
        useJournalForAffirmations = useJournalForAffirmations,
    )
}
