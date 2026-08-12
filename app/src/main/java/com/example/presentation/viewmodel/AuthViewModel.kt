package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.IshcDatabase
import com.example.data.repository.AuthRepositoryImpl
import com.example.domain.model.UserDomainModel
import com.example.domain.model.UserRole
import com.example.domain.usecase.AuthenticateUserUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    val user: UserDomainModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val db = IshcDatabase.getDatabase(application)
    private val authRepo = AuthRepositoryImpl(db.ishcDao())
    private val authenticateUserUseCase = AuthenticateUserUseCase(authRepo)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authenticateUserUseCase.getAuthenticatedUser().collect { userDomain ->
                if (userDomain != null && userDomain.id != "guest_user") {
                    _uiState.update { it.copy(user = userDomain, isAuthenticated = true) }
                } else {
                    _uiState.update { it.copy(user = null, isAuthenticated = false) }
                }
            }
        }
    }

    fun loginWithGoogle(idToken: String, email: String, name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authenticateUserUseCase.loginWithGoogle(idToken, email, name)
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            user = user,
                            isAuthenticated = true,
                            isLoading = false,
                            successMessage = "Google Sign-In Successful! Welcome ${user.name}"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Google Sign-In failed"
                        )
                    }
                }
            )
        }
    }

    fun loginWithEmail(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter both email and password") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authenticateUserUseCase.loginWithEmail(email, pass)
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            user = user,
                            isAuthenticated = true,
                            isLoading = false,
                            successMessage = "Authenticated as ${user.name} (${user.role})"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Authentication failed"
                        )
                    }
                }
            )
        }
    }

    fun registerWithEmail(email: String, pass: String, name: String, role: UserRole) {
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all registration fields") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authenticateUserUseCase.registerWithEmail(email, pass, name, role)
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            user = user,
                            isAuthenticated = true,
                            isLoading = false,
                            successMessage = "Account registered successfully as ${user.role}!"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Registration failed"
                        )
                    }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticateUserUseCase.logout()
            _uiState.update {
                AuthUiState(
                    user = null,
                    isAuthenticated = false,
                    successMessage = "Logged out safely"
                )
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter your email to receive password reset link") }
            return
        }
        viewModelScope.launch {
            authenticateUserUseCase.resetPassword(email)
            _uiState.update { it.copy(successMessage = "Password reset instructions sent to $email") }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
