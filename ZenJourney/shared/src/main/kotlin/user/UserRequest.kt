package user

import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt

data class UserRequest(
    val name: String,
    val email: String,
    val password: String
)

fun UserRequest.toDomain(): User {
    return User(
        id = ObjectId(),
        name = name,
        email = email,
        password = BCrypt.hashpw(password, BCrypt.gensalt()),
    )
}