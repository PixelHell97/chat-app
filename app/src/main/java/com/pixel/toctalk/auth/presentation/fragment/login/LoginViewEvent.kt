package com.pixel.toctalk.auth.presentation.fragment.login

import com.pixel.toctalk.auth.domain.models.AuthUser
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.domain.util.AuthError

sealed class LoginViewEvent {
    data class NavigateToHome(
        val authUser: AuthUser,
    ) : LoginViewEvent()

    data class Error(
        val authError: AuthError? = null,
        val emailValidationError: ValidationError = ValidationError.NONE,
        val passwordValidationError: ValidationError = ValidationError.NONE,
    ) : LoginViewEvent()
}
