package com.pixel.toctalk.ui.auth.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.auth.fragment.InputState
import com.pixel.toctalk.ui.base.BaseViewModel
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel

class LoginViewModel : BaseViewModel() {
    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()
    val auth = Firebase.auth
    val isLoading = MutableLiveData(false)

    private val _event = MutableLiveData<LoginViewEvent>()
    val event: LiveData<LoginViewEvent> = _event

    fun login() {
        if (isLoading.value == true) return
        if (!isValidInputs()) return
        isLoading.value = true
        auth.signInWithEmailAndPassword(
            emailLiveData.value!!,
            passwordLiveData.value!!,
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                getUser(user!!.uid)
            } else {
                isLoading.value = false
                _messageDialogModelDialog.value = MessageDialogModel(
                    message = task.exception?.localizedMessage ?: "Unable to Login",
                )
            }
        }
    }

    private fun getUser(uid: String?) {
        MyDatabase
            .getUser(uid) { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    val user = task.result.toObject(User::class.java)
                    _event.value = LoginViewEvent.NavigateToHome(user!!)
                } else {
                    _messageDialogModelDialog.value = MessageDialogModel(
                        message = task.exception?.localizedMessage
                            ?: "Unable to login\nPlease try again",
                    )
                }
            }
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
