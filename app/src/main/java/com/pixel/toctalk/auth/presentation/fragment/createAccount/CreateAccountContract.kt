package com.pixel.toctalk.auth.presentation.fragment.createAccount

import android.net.Uri

data class CreateAccountState(
    val isLoading: Boolean = false,
)

sealed interface CreateAccountAction {
    data class Register(
        val email: String,
        val password: String,
    ) : CreateAccountAction

    data class UpdateUserData(
        val username: String,
        val userImage: Uri?,
    ) : CreateAccountAction
}
