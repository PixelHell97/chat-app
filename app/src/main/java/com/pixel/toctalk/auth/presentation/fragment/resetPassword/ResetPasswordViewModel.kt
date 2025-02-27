package com.pixel.toctalk.auth.presentation.fragment.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixel.toctalk.auth.domain.AuthenticationRepository
import com.pixel.toctalk.auth.domain.usecase.ValidationUseCase
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.domain.util.AuthError
import com.pixel.toctalk.core.domain.util.SingleLiveEvent
import com.pixel.toctalk.core.domain.util.onError
import com.pixel.toctalk.core.domain.util.onSuccess
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val validationUseCase: ValidationUseCase,
) : ViewModel() {
    private val userEmail = MutableLiveData<String>()
    private val resetPasswordCode = MutableLiveData<String>()

    private val _event = SingleLiveEvent<ResetPasswordEvent>()
    val event: LiveData<ResetPasswordEvent> = _event

    fun sendResetPasswordCodeWithEmail() {
        val emailValidationError = validationUseCase.isValidEmail(userEmail.value!!)
        if (emailValidationError == ValidationError.NONE) {
            viewModelScope.launch {
                authenticationRepository
                    .sendResetPasswordCodeWithEmail(userEmail.value!!)
                    .onSuccess { isSent ->
                        if (isSent == true) {
                            _event.postValue(ResetPasswordEvent.NavigateToConfirmResetPasswordCode)
                        } else {
                            _event.postValue(ResetPasswordEvent.Error(AuthError.UNKNOWN))
                        }
                    }.onError { error ->
                        _event.postValue(
                            ResetPasswordEvent.Error(
                                authError = error,
                            ),
                        )
                    }
            }
        } else {
            _event.value =
                ResetPasswordEvent.Error(
                    emailValidationError = emailValidationError,
                )
        }
    }

    fun updateEmail(email: CharSequence) {
        userEmail.value = email.toString()
    }
}
