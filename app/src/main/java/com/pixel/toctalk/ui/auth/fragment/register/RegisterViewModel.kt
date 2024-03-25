package com.pixel.toctalk.ui.auth.fragment.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.auth.fragment.InputState
import com.pixel.toctalk.ui.base.BaseViewModel
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel

class RegisterViewModel : BaseViewModel() {
    val auth = Firebase.auth
    val userProfilePicLiveData = MutableLiveData<Uri>(null)
    val usernameLiveData = MutableLiveData<String>()
    val usernameError = MutableLiveData<String?>()
    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmationLiveData = MutableLiveData<String>()
    val passwordConfirmationError = MutableLiveData<String?>()
    val isLoading = MutableLiveData(false)

    private val _event = MutableLiveData<RegisterViewEvent>()
    val event: LiveData<RegisterViewEvent> = _event

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
                _messageDialogModelDialog.value = MessageDialogModel(
                    message = task.exception?.localizedMessage ?: "Unable to create new account",
                )
            }
        }
    }

    fun setProfilePic(imageUri: Uri) {
        userProfilePicLiveData.value = imageUri
    }

    private fun createUserInDB(uid: String) {
        if (userProfilePicLiveData.value != null) {
            MyDatabase.uploadUserPic(uid, userProfilePicLiveData.value) {
                if (it.isSuccessful) {
                    val user = User(
                        uid = uid,
                        username = usernameLiveData.value!!,
                        email = emailLiveData.value!!,
                        profilePic = it.result.toString(),
                    )
                    MyDatabase
                        .createUser(user) { task ->
                            isLoading.value = false
                            if (task.isSuccessful) {
                                _event.value = RegisterViewEvent.NavigateToLogin
                            } else {
                                _messageDialogModelDialog.value = MessageDialogModel(
                                    message = task.exception?.localizedMessage
                                        ?: "Unable to create new account\nPlease try again",
                                )
                            }
                        }
                } else {
                    _messageDialogModelDialog.value = MessageDialogModel(
                        message = it.exception?.localizedMessage ?: "Unable to upload this photo",
                        posActionName = "Ok",
                    )
                }
            }
        } else {
            val user = User(
                uid = uid,
                username = usernameLiveData.value!!,
                email = emailLiveData.value!!,
            )
            MyDatabase
                .createUser(user) { task ->
                    isLoading.value = false
                    if (task.isSuccessful) {
                        _event.value = RegisterViewEvent.NavigateToLogin
                    } else {
                        _messageDialogModelDialog.value = MessageDialogModel(
                            message = task.exception?.localizedMessage
                                ?: "Unable to create new account\nPlease try again",
                        )
                    }
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
