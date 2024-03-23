package user

import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val pin: String = "",
    val useJournalForAffirmations: Boolean = false,
)

fun UserRequest.toDomain(): User {
    return User(
        id = ObjectId(),
        name = name,
        email = email,
        password = BCrypt.hashpw(password, BCrypt.gensalt()),
        pin = pin,
        useJournalForAffirmations = useJournalForAffirmations,
    )
}