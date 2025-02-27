package com.pixel.toctalk.auth.presentation.fragment.createAccount

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixel.toctalk.auth.domain.AuthenticationRepository
import com.pixel.toctalk.auth.domain.usecase.ValidationUseCase
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.domain.util.SingleLiveEvent
import com.pixel.toctalk.core.domain.util.onError
import com.pixel.toctalk.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthenticationRepository,
    private val validationUseCase: ValidationUseCase,
) : ViewModel() {
//    private val _currentUser: MutableStateFlow<User> =
//        MutableStateFlow(
//            User(),
//        )
//    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _stateFlow: MutableStateFlow<CreateAccountState> =
        MutableStateFlow(CreateAccountState())

    val stateFlow: StateFlow<CreateAccountState> = _stateFlow.asStateFlow()

    private val _event = SingleLiveEvent<RegisterViewEvent>()
    val event: LiveData<RegisterViewEvent> = _event

    fun handleAction(action: CreateAccountAction) {
        when (action) {
            is CreateAccountAction.Register -> {
                createAccount(
                    email = action.email,
                    password = action.password,
                )
            }

            is CreateAccountAction.UpdateUserData -> {
                updateProfile(
                    username = action.username,
                    profilePic = action.userImage,
                )
            }
        }
    }

    private fun createAccount(
        email: String,
        password: String,
    ) {
        val emailError = validationUseCase.isValidEmail(email)
        val passwordError = validationUseCase.isValidPassword(password)

        if (emailError != ValidationError.NONE || passwordError != ValidationError.NONE) {
            _event.postValue(
                RegisterViewEvent.Error(
                    emailValidationError = emailError,
                    passwordValidationError = passwordError,
                ),
            )
            return
        }

        _stateFlow.update {
            it.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch {
            authRepository
                .createAccount(
                    email = email,
                    password = password,
                ).onSuccess { user ->
                    // _currentUser.value = user
//                    _currentUser.update {
//                        it.copy(
//                            uid = user.uid,
//                            email = user.email,
//                            isEmailVerified = user.isEmailVerified,
//                        )
//                    }
                    _event.postValue(RegisterViewEvent.Navigate(user.uid))
                }.onError { error ->
                    _event.postValue(
                        RegisterViewEvent.Error(
                            authError = error,
                        ),
                    )
                }.also {
                    _stateFlow.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun updateProfile(
        username: String,
        profilePic: Uri?,
    ) {
        val usernameError = validationUseCase.isValidUserName(username)

        if (usernameError != ValidationError.NONE) {
            _event.postValue(
                RegisterViewEvent.Error(
                    nameValidationError = usernameError,
                ),
            )
            return
        }

        _stateFlow.update {
            it.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch {
            authRepository
                .updateProfile(username, profilePic)
                .onSuccess { user ->
//                    _currentUser.update {
//                        it.copy(
//                            displayName = user.displayName,
//                            profilePic = user.profilePic,
//                        )
//                    }
                    val profilePicUrl =
                        profilePic?.let {
                            authRepository.uploadUserImageToStorage(user.uid.toString(), it)
                        }
                    authRepository
                        .createUserInFireStore(user.copy(profilePic = profilePicUrl?.toUri()))
                        .onSuccess { isCreated ->
                            if (isCreated) {
                                _event.postValue(RegisterViewEvent.Navigate(user.uid))
                            } else {
                                Log.d("TAG", "createUserInFireStore: Error")
                            }
                        }.onError { error ->
                            // TODO: Handle error
                            Log.d("TAG", "createUserInFireStore: $error")
                        }.also {
                            _stateFlow.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }
                        }
                }.onError { error ->
                    _event.postValue(
                        RegisterViewEvent.Error(
                            authError = error,
                        ),
                    )
                }.also {
                    _stateFlow.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

    /*fun onRegisterClick() {
        if (isLoading.value == true) return
        // if (!isValid()) return
        isLoading.value = true
        auth
            .createUserWithEmailAndPassword(
                emailLiveData.value!!,
                passwordLiveData.value!!,
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    createUserInDB(user!!.uid)
                } else {
                    isLoading.value = false
                    _messageDialogModelDialog.value =
                        MessageDialogModel(
                            message =
                                task.exception?.localizedMessage
                                    ?: "Unable to create new account",
                        )
                }
            }
    }*/

    /*private fun createUserInDB(uid: String) {
        if (userProfilePicLiveData.value != null) {
            UserMdb.uploadUserPic(uid, userProfilePicLiveData.value) {
                if (it.isSuccessful) {
                    val user =
                        User(
                            uid = uid,
                            username = usernameLiveData.value!!,
                            email = emailLiveData.value!!,
                            profilePic = it.result.toString(),
                        )
                    UserMdb
                        .createUser(user) { task ->
                            isLoading.value = false
                            if (task.isSuccessful) {
                                _event.value = RegisterViewEvent.NavigateToLogin
                            } else {
                                _messageDialogModelDialog.value =
                                    MessageDialogModel(
                                        message =
                                            task.exception?.localizedMessage
                                                ?: "Unable to create new account\nPlease try again",
                                    )
                            }
                        }
                } else {
                    _messageDialogModelDialog.value =
                        MessageDialogModel(
                            message =
                                it.exception?.localizedMessage
                                    ?: "Unable to upload this photo",
                            posActionName = "Ok",
                        )
                }
            }
        } else {
            val user =
                User(
                    uid = uid,
                    username = usernameLiveData.value!!,
                    email = emailLiveData.value!!,
                )
            UserMdb
                .createUser(user) { task ->
                    isLoading.value = false
                    if (task.isSuccessful) {
                        _event.value = RegisterViewEvent.NavigateToLogin
                    } else {
                        _messageDialogModelDialog.value =
                            MessageDialogModel(
                                message =
                                    task.exception?.localizedMessage
                                        ?: "Unable to create new account\nPlease try again",
                            )
                    }
                }
        }
    }*/

//    private fun isValid(): Boolean {
//        var isValid = true
//        if (!(isValidInput(usernameLiveData.value, InputState.UsernameInput))) isValid = false
//        if (!(isValidInput(emailLiveData.value, InputState.EmailInput))) isValid = false
//        if (!(isValidInput(passwordLiveData.value, InputState.PasswordInput))) isValid = false
//        if (!(
//                isValidInput(
//                    passwordConfirmationLiveData.value,
//                    InputState.PasswordConfirmationInput,
//                )
//            )
//        ) {
//            isValid = false
//        }
//        return isValid
//    }
//
//    fun isValidInput(
//        input: CharSequence?,
//        inputType: InputState,
//    ): Boolean {
//        var isValid = true
//        when (inputType) {
//            is InputState.UsernameInput -> {
//                if (input.isNullOrEmpty()) {
//                    usernameError.value = "Username required"
//                    isValid = false
//                } else {
//                    usernameError.value = null
//                }
//            }
//
//            is InputState.EmailInput -> {
//                if (input.isNullOrEmpty()) {
//                    emailError.value = "Email required"
//                    isValid = false
//                } else {
//                    emailError.value = null
//                }
//            }
//
//            is InputState.PasswordInput -> {
//                if (input.isNullOrEmpty()) {
//                    passwordError.value = "Password required"
//                    isValid = false
//                } else if (input.length < 6) {
//                    passwordError.value = "Password length over 6"
//                    isValid = false
//                } else {
//                    passwordError.value = null
//                }
//            }
//
//            is InputState.PasswordConfirmationInput -> {
//                if (input.isNullOrEmpty()) {
//                    passwordConfirmationError.value = "Confirm password required"
//                    isValid = false
//                } else if (input != passwordLiveData.value) {
//                    passwordConfirmationError.value = "Password doesn't match"
//                    isValid = false
//                } else {
//                    passwordConfirmationError.value = null
//                }
//            }
//        }
//        return isValid
//    }
}
