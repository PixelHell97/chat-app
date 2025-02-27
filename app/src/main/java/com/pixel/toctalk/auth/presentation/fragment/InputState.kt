package com.pixel.toctalk.auth.presentation.fragment

sealed class InputState {
    data object UsernameInput : InputState()
    data object EmailInput : InputState()
    data object PasswordInput : InputState()
    data object PasswordConfirmationInput : InputState()
}
