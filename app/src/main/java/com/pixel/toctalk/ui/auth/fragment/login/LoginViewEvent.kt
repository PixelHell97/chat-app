package com.pixel.toctalk.ui.auth.fragment.login

import com.pixel.toctalk.data.model.User

sealed class LoginViewEvent {

    // data object NavigateToReg : LoginViewEvent()
    data class NavigateToHome(val user: User) : LoginViewEvent()
}
