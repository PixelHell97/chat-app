package com.pixel.toctalk.auth.presentation.fragment.createAccount

import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.domain.util.AuthError

sealed class RegisterViewEvent {
    data class Navigate(
        val uid: String? = null,
    ) : RegisterViewEvent()

    data class Error(
        val authError: AuthError? = null,
        val emailValidationError: ValidationError = ValidationError.NONE,
        val passwordValidationError: ValidationError = ValidationError.NONE,
        val nameValidationError: ValidationError = ValidationError.NONE,
    ) : RegisterViewEvent()
}
