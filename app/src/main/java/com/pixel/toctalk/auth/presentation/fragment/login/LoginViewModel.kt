package com.pixel.toctalk.auth.presentation.fragment.login

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

class LoginViewModel(
    private val authRepository: AuthenticationRepository,
    private val validationUseCase: ValidationUseCase,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())

    val stateFlow: StateFlow<LoginState> = _stateFlow.asStateFlow()

    private val _event = SingleLiveEvent<LoginViewEvent>()
    val event: LiveData<LoginViewEvent> = _event

    fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.Login -> {
                _stateFlow.update {
                    it.copy(
                        isLoading = true,
                    )
                }
                login(action.email, action.password)
            }
        }
    }

    private fun login(
        email: String,
        password: String,
    ) {
        val emailError = validationUseCase.isValidEmail(email)
        if (
            emailError == ValidationError.NONE
        ) {
            viewModelScope.launch {
                authRepository
                    .login(
                        email = email,
                        password = password,
                    ).onSuccess { user ->
                        _stateFlow.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _event.postValue(LoginViewEvent.NavigateToHome(user))
                    }.onError { error ->
                        _stateFlow.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _event.postValue(
                            LoginViewEvent.Error(
                                authError = error,
                            ),
                        )
                    }
            }
        } else {
            _stateFlow.update {
                it.copy(
                    isLoading = false,
                )
            }

            _event.postValue(
                LoginViewEvent.Error(
                    emailValidationError = emailError,
                ),
            )
        }
    }
}
