package com.pixel.toctalk.auth.presentation.fragment.login

data class LoginState(
    val isLoading: Boolean = false,
)

sealed interface LoginAction {
    data class Login(
        val email: String,
        val password: String,
    ) : LoginAction
}
