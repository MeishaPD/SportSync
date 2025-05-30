package brawijaya.example.sportsync.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val id: String,
    val email: String?,
    val phone: String?,
    val createdAt: String
)

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}