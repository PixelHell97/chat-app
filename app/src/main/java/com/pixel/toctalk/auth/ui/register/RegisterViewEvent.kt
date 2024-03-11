package com.pixel.toctalk.auth.ui.register

sealed class RegisterViewEvent {
    data object NavigateToLogin : RegisterViewEvent()
}
