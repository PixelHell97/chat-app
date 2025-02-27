package com.pixel.toctalk.auth.presentation.fragment.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.core.presentation.BaseAuthFragment
import com.pixel.toctalk.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseAuthFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun initViewModel(): LoginViewModel = viewModel<LoginViewModel>().value

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginViewEvent.NavigateToHome -> {
                    val user = event.authUser
                    navigateToHome(user.uid)
                }

                is LoginViewEvent.Error -> {
                    event.authError?.let { error ->
                        handleAuthError(error)
                    }
                    handleEmailError(event.emailValidationError)
                    handlePasswordError(event.passwordValidationError)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                binding.isLoading = state.isLoading
            }
        }
    }

    private fun handlePasswordError(passwordError: ValidationError) {
        binding.logPasswordField.error =
            when (passwordError) {
                ValidationError.EMPTY_PASSWORD -> getString(R.string.field_is_required)
                else -> null
            }
    }

    private fun handleEmailError(emailError: ValidationError) {
        binding.logEmailField.error =
            when (emailError) {
                ValidationError.EMPTY_EMAIL -> getString(R.string.field_is_required)
                ValidationError.INVALID_EMAIL -> getString(R.string.invalid_email)
                else -> null
            }
    }

    private fun initViews() {
        binding.logEmail.doOnTextChanged { email, _, _, _ ->
            if (!email.isNullOrEmpty()) {
                binding.logEmailField.error = null
            }
        }
        binding.logPassword.doOnTextChanged { password, _, _, _ ->
            if (!password.isNullOrEmpty()) {
                binding.logPasswordField.error = null
            }
        }
        binding.btnLogin.setOnClickListener {
            val email =
                binding.logEmail.text
                    ?.toString()
                    .orEmpty()
            val password =
                binding.logPassword.text
                    ?.toString()
                    .orEmpty()

            val errors = mutableListOf<Pair<(ValidationError) -> Unit, ValidationError>>()

            if (email.isEmpty()) errors.add(::handleEmailError to ValidationError.EMPTY_EMAIL)
            if (password.isEmpty()) errors.add(::handlePasswordError to ValidationError.EMPTY_PASSWORD)

            if (errors.isNotEmpty()) {
                errors.forEach { (handler, error) -> handler(error) }
                return@setOnClickListener
            }

            viewModel.handleAction(LoginAction.Login(email, password))
        }
        binding.forgetPassword.setOnClickListener {
            navigateToForgetPassword()
        }
        binding.createAccount.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToForgetPassword() {
        findNavController()
            .navigate(R.id.action_loginFragment_to_sendResetPasswordCodeFragment)
    }

    private fun navigateToRegister() {
        findNavController()
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }
}
