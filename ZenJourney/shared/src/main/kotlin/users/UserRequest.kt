package users

import org.bson.types.ObjectId

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
        password = "password"
    )
}