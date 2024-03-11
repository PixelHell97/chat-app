package com.pixel.toctalk.auth.ui

sealed class InputState {
    data object UsernameInput : InputState()
    data object EmailInput : InputState()
    data object PasswordInput : InputState()
    data object PasswordConfirmationInput : InputState()
}
