package com.pixel.toctalk.auth.presentation.fragment.createAccount.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.auth.presentation.fragment.createAccount.CreateAccountAction
import com.pixel.toctalk.auth.presentation.fragment.createAccount.RegisterViewEvent
import com.pixel.toctalk.auth.presentation.fragment.createAccount.RegisterViewModel
import com.pixel.toctalk.core.presentation.BaseAuthFragment
import com.pixel.toctalk.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseAuthFragment<FragmentRegisterBinding, RegisterViewModel>() {
    override fun initViewModel(): RegisterViewModel = viewModel<RegisterViewModel>().value

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner, ::onEventChange)
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                binding.isLoading = state.isLoading
            }
        }
    }

    private fun onEventChange(event: RegisterViewEvent) {
        when (event) {
            is RegisterViewEvent.Navigate -> {
                findNavController()
                    .navigate(R.id.action_registerFragment_to_completeProfileFragment)
            }

            is RegisterViewEvent.Error -> {
                event.authError?.let { error ->
                    handleAuthError(error)
                }
                handleEmailError(event.emailValidationError)
                handlePasswordError(event.passwordValidationError)
            }
        }
    }

    private fun initViews() {
        binding.regEmail.doOnTextChanged { email, _, _, _ ->
            if (!email.isNullOrEmpty()) {
                binding.regEmailField.error = null
            }
        }
        binding.regPassword.doOnTextChanged { password, _, _, _ ->
            if (!password.isNullOrEmpty()) {
                binding.regPasswordField.error = null
            }
        }
        binding.regPasswordConfirm.doOnTextChanged { confirmPassword, _, _, _ ->
            if (!confirmPassword.isNullOrEmpty()) {
                binding.regPasswordConfirmField.error = null
            }
        }

        binding.btnRegister.setOnClickListener {
            val email =
                binding.regEmail.text
                    ?.toString()
                    .orEmpty()
            val password =
                binding.regPassword.text
                    ?.toString()
                    .orEmpty()
            val confirmPassword =
                binding.regPasswordConfirm.text
                    ?.toString()
                    .orEmpty()

            val errors = mutableListOf<Pair<(ValidationError) -> Unit, ValidationError>>()

            if (email.isEmpty()) errors.add(::handleEmailError to ValidationError.EMPTY_EMAIL)
            if (password.isEmpty()) errors.add(::handlePasswordError to ValidationError.EMPTY_PASSWORD)
            if (confirmPassword.isEmpty()) errors.add(::handlePasswordConfirmError to ValidationError.EMPTY_PASSWORD)
            if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                errors.add(::handlePasswordConfirmError to ValidationError.UNMATCHED_PASSWORD)
            }

            if (errors.isNotEmpty()) {
                errors.forEach { (handler, error) -> handler(error) }
                return@setOnClickListener
            }

            viewModel.handleAction(CreateAccountAction.Register(email, password))
        }
    }

    private fun handlePasswordConfirmError(confirmPassword: ValidationError) {
        binding.regPasswordConfirmField.error =
            when (confirmPassword) {
                ValidationError.EMPTY_PASSWORD -> getString(R.string.field_is_required)
                ValidationError.UNMATCHED_PASSWORD -> getString(R.string.unmatched_password)
                else -> null
            }
    }

    private fun handlePasswordError(passwordError: ValidationError) {
        binding.regPasswordField.error =
            when (passwordError) {
                ValidationError.EMPTY_PASSWORD -> getString(R.string.field_is_required)
                ValidationError.INVALID_PASSWORD -> getString(R.string.invalid_password)
                else -> null
            }
    }

    private fun handleEmailError(emailError: ValidationError) {
        binding.regEmailField.error =
            when (emailError) {
                ValidationError.EMPTY_EMAIL -> getString(R.string.field_is_required)
                ValidationError.INVALID_EMAIL -> getString(R.string.invalid_email)
                else -> null
            }
    }
}
