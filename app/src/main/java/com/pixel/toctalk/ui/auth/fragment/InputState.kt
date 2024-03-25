package com.pixel.toctalk.ui.auth.fragment

sealed class InputState {
    data object UsernameInput : InputState()
    data object EmailInput : InputState()
    data object PasswordInput : InputState()
    data object PasswordConfirmationInput : InputState()
}
