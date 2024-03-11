package com.pixel.toctalk.auth.ui.register

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.auth.database.MyDatabase
import com.pixel.toctalk.auth.database.User
import com.pixel.toctalk.auth.extensions.model.Message
import com.pixel.toctalk.auth.ui.InputState
import com.pixel.toctalk.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {
    val usernameLiveData = MutableLiveData<String>()
    val usernameError = MutableLiveData<String?>()
    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmationLiveData = MutableLiveData<String>()
    val passwordConfirmationError = MutableLiveData<String?>()
    val auth = Firebase.auth
    val isLoading = MutableLiveData(false)
    val event = MutableLiveData<RegisterViewEvent>()

    fun onRegisterClick() {
        if (isLoading.value == true) return
        if (!isValid()) return
        isLoading.value = true
        auth.createUserWithEmailAndPassword(
            emailLiveData.value!!,
            passwordLiveData.value!!,
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                createUserInDB(user!!.uid)
            } else {
                isLoading.value = false
                messageDialog.value = Message(
                    title = "Error",
                    message = task.exception?.localizedMessage ?: "Unable to create new account",
                )
            }
        }
    }

    private fun createUserInDB(uid: String) {
        val user = User(
            uid = uid,
            username = usernameLiveData.value!!,
            email = emailLiveData.value!!,
        )
        MyDatabase
            .createUser(user) { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    event.postValue(RegisterViewEvent.NavigateToLogin)
                } else {
                    messageDialog.value = Message(
                        title = "Creation Error",
                        message = task.exception?.localizedMessage
                            ?: "Unable to create new account\nPlease try again",
                    )
                }
            }
    }

    private fun isValid(): Boolean {
        var isValid = true
        if (!(isValidInput(usernameLiveData.value, InputState.UsernameInput))) isValid = false
        if (!(isValidInput(emailLiveData.value, InputState.EmailInput))) isValid = false
        if (!(isValidInput(passwordLiveData.value, InputState.PasswordInput))) isValid = false
        if (!(
                isValidInput(
                    passwordConfirmationLiveData.value,
                    InputState.PasswordConfirmationInput,
                )
                )
        ) {
            isValid = false
        }
        return isValid
    }

    fun isValidInput(input: CharSequence?, inputType: InputState): Boolean {
        var isValid = true
        when (inputType) {
            is InputState.UsernameInput -> {
                if (input.isNullOrEmpty()) {
                    usernameError.value = "Username required"
                    isValid = false
                } else {
                    usernameError.value = null
                }
            }

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
                } else if (input.length < 6) {
                    passwordError.value = "Password length over 6"
                    isValid = false
                } else {
                    passwordError.value = null
                }
            }

            is InputState.PasswordConfirmationInput -> {
                if (input.isNullOrEmpty()) {
                    passwordConfirmationError.value = "Confirm password required"
                    isValid = false
                } else if (input != passwordLiveData.value) {
                    passwordConfirmationError.value = "Password doesn't match"
                    isValid = false
                } else {
                    passwordConfirmationError.value = null
                }
            }
        }
        return isValid
    }
}
