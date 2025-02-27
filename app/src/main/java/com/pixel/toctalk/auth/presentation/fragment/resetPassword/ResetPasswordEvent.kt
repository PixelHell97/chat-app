package com.pixel.toctalk.auth.presentation.fragment.resetPassword

import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.domain.util.AuthError

sealed class ResetPasswordEvent {
    data object NavigateToConfirmResetPasswordCode : ResetPasswordEvent()

    data class Error(
        val authError: AuthError? = null,
        val emailValidationError: ValidationError = ValidationError.NONE,
    ) : ResetPasswordEvent()
}
