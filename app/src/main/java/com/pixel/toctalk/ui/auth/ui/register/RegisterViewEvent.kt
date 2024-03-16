package com.pixel.toctalk.ui.auth.ui.register

sealed class RegisterViewEvent {
    data object NavigateToLogin : RegisterViewEvent()
}
