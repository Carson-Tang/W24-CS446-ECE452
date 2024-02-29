package users

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
)