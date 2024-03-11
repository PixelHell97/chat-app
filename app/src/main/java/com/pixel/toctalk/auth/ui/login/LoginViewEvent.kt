package com.pixel.toctalk.auth.ui.login

import com.pixel.toctalk.auth.database.User

sealed class LoginViewEvent {

    data object NavigateToReg : LoginViewEvent()
    data class NavigateToHome(val user: User) : LoginViewEvent()
}
