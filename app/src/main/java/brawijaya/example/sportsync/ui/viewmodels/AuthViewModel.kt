package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.AuthResult
import brawijaya.example.sportsync.data.models.AuthUser
import brawijaya.example.sportsync.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

data class AuthState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val user: AuthUser? = null,
    val isInitialized: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Starting to observe auth state...")

                authRepository.observeAuthState().collect { isAuthenticated ->
                    val currentUser = if (isAuthenticated) authRepository.getCurrentUser() else null

                    Log.d("AuthViewModel", "Auth state changed - isAuthenticated: $isAuthenticated, user: $currentUser")

                    _authState.value = AuthState(
                        isLoading = false,
                        isAuthenticated = isAuthenticated,
                        user = currentUser,
                        isInitialized = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to observe auth state", e)
                _authState.value = AuthState(
                    isLoading = false,
                    isAuthenticated = false,
                    user = null,
                    isInitialized = true,
                    error = "Failed to observe auth state: ${e.message}"
                )
            }
        }
    }

    fun signUp(
        fullName: String,
        username: String,
        email: String,
        birthDate: String,
        phone: String,
        password: String,
        useEmail: Boolean
    ) {
        viewModelScope.launch {
            val flow = if (useEmail) {
                authRepository.signUpWithEmail(
                    email = email,
                    password = password,
                    fullName = fullName,
                    username = username,
                    phone = phone,
                    birthDate = birthDate
                )
            } else {
                authRepository.signUpWithPhone(
                    phone = phone,
                    password = password,
                    fullName = fullName,
                    username = username,
                    email = email,
                    birthDate = birthDate
                )
            }

            flow.collect { result ->
                when (result) {
                    is AuthResult.Loading -> {
                        _authState.value = _authState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is AuthResult.Success -> {
                        // Auth state will be updated automatically through observeAuthState()
                    }
                    is AuthResult.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun signIn(
        emailOrPhone: String,
        password: String,
        isEmail: Boolean
    ) {
        viewModelScope.launch {
            val flow = if (isEmail) {
                authRepository.signInWithEmail(emailOrPhone, password)
            } else {
                authRepository.signInWithPhone(emailOrPhone, password)
            }

            flow.collect { result ->
                when (result) {
                    is AuthResult.Loading -> {
                        _authState.value = _authState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is AuthResult.Success -> {
                        // Auth state will be updated automatically through observeAuthState()
                    }
                    is AuthResult.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut().collect { result ->
                when (result) {
                    is AuthResult.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true)
                    }
                    is AuthResult.Success -> {
                        // Auth state will be updated automatically through observeAuthState()
                    }
                    is AuthResult.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}