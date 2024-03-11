package com.pixel.toctalk.auth.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.auth.database.MyDatabase
import com.pixel.toctalk.auth.database.User
import com.pixel.toctalk.auth.extensions.model.Message
import com.pixel.toctalk.auth.ui.InputState
import com.pixel.toctalk.base.BaseViewModel

class LoginViewModel : BaseViewModel() {
    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()
    val auth = Firebase.auth
    val isLoading = MutableLiveData(false)
    val event = MutableLiveData<LoginViewEvent>()

    fun login() {
        if (isLoading.value == true) return
        if (!isValidInputs()) return
        isLoading.value = false
        auth.signInWithEmailAndPassword(
            emailLiveData.value!!,
            passwordLiveData.value!!,
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                getUser(user!!.uid)
            } else {
                isLoading.value = false
                messageDialog.value = Message(
                    title = "Something went wrong",
                    message = task.exception?.localizedMessage ?: "Unable to Login",
                )
            }
        }
    }

    private fun getUser(uid: String?) {
        MyDatabase
            .getUser(uid) { task ->
                isLoading.value = false
                val user = task.result.toObject(User::class.java)
                if (task.isSuccessful) {
                    event.postValue(LoginViewEvent.NavigateToHome(user!!))
                } else {
                    messageDialog.value = Message(
                        title = "Login Error",
                        message = task.exception?.localizedMessage
                            ?: "Unable to login\nPlease try again",
                    )
                }
            }
    }

    fun navToReg() {
        event.postValue(LoginViewEvent.NavigateToReg)
    }

    private fun isValidInputs(): Boolean {
        var isValid = true
        if (!(isValidInput(emailLiveData.value, InputState.EmailInput))) isValid = false
        if (!(isValidInput(passwordLiveData.value, InputState.PasswordInput))) isValid = false
        return isValid
    }

    fun isValidInput(input: CharSequence?, inputType: InputState): Boolean {
        var isValid = true
        when (inputType) {
            is InputState.EmailInput -> {
                if (input.isNullOrEmpty()) {
                    emailError.value = "Email required"
                    isValid = false
                } else {
                    emailError.value = null
                }
            }

            is InputState.PasswordInput -> {
                if (input.isNullOrEmpty()) {
                    passwordError.value = "Password required"
                    isValid = false
                } else {
                    passwordError.value = null
                }
            }

            else -> {}
        }
        return isValid
    }
}
