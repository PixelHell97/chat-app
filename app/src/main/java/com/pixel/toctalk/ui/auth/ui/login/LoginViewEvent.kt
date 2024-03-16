package com.pixel.toctalk.ui.auth.ui.login

import com.pixel.toctalk.data.database.model.User

sealed class LoginViewEvent {

    data object NavigateToReg : LoginViewEvent()
    data class NavigateToHome(val user: User) : LoginViewEvent()
}
