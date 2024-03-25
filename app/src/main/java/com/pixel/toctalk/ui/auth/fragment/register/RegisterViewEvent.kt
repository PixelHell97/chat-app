package com.pixel.toctalk.ui.auth.fragment.register

sealed class RegisterViewEvent {
    data object NavigateToLogin : RegisterViewEvent()
}
