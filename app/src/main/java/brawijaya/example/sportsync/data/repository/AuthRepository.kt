package brawijaya.example.sportsync.data.repository

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.Phone
import io.github.jan.supabase.gotrue.SessionStatus
import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.AuthResult
import brawijaya.example.sportsync.data.models.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val auth = SupabaseClient.client.auth

    fun observeAuthState(): Flow<Boolean> {
        return auth.sessionStatus.map { sessionStatus ->
            when (sessionStatus) {
                is SessionStatus.Authenticated -> true
                is SessionStatus.NotAuthenticated -> false
                is SessionStatus.LoadingFromStorage -> false
                is SessionStatus.NetworkError -> false
            }
        }
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        fullName: String,
        username: String,
        phone: String,
        birthDate: String
    ): Flow<AuthResult> = flow {
        try {
            emit(AuthResult.Loading)

            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("full_name", JsonPrimitive(fullName))
                    put("username", JsonPrimitive(username))
                    put("phone", JsonPrimitive(phone))
                    put("birth_date", JsonPrimitive(birthDate))
                }
            }

            emit(AuthResult.Success)
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Sign up failed"))
        }
    }

    suspend fun signUpWithPhone(
        phone: String,
        password: String,
        fullName: String,
        username: String,
        email: String,
        birthDate: String
    ): Flow<AuthResult> = flow {
        try {
            emit(AuthResult.Loading)

            auth.signUpWith(Phone) {
                this.phone = phone
                this.password = password
                data = buildJsonObject {
                    put("full_name", JsonPrimitive(fullName))
                    put("username", JsonPrimitive(username))
                    put("email", JsonPrimitive(email))
                    put("birth_date", JsonPrimitive(birthDate))
                }
            }

            emit(AuthResult.Success)
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Sign up failed"))
        }
    }

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Flow<AuthResult> = flow {
        try {
            emit(AuthResult.Loading)

            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            emit(AuthResult.Success)
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Login failed"))
        }
    }

    suspend fun signInWithPhone(
        phone: String,
        password: String
    ): Flow<AuthResult> = flow {
        try {
            emit(AuthResult.Loading)

            auth.signInWith(Phone) {
                this.phone = phone
                this.password = password
            }

            emit(AuthResult.Success)
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Login failed"))
        }
    }

    suspend fun signOut(): Flow<AuthResult> = flow {
        try {
            emit(AuthResult.Loading)
            auth.signOut()
            emit(AuthResult.Success)
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Sign out failed"))
        }
    }

    fun getCurrentUser(): AuthUser? {
        return auth.currentUserOrNull()?.let { user ->
            AuthUser(
                id = user.id,
                email = user.email,
                phone = user.phone,
                createdAt = user.createdAt.toString()
            )
        }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUserOrNull() != null
}