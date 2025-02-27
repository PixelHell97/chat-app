package com.pixel.toctalk.auth.presentation.fragment.resetPassword.sendCode

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.auth.presentation.fragment.resetPassword.ResetPasswordEvent
import com.pixel.toctalk.auth.presentation.fragment.resetPassword.ResetPasswordViewModel
import com.pixel.toctalk.core.domain.util.AuthError
import com.pixel.toctalk.core.presentation.BaseFragment
import com.pixel.toctalk.databinding.FragmentSendResetPasswordCodeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SendResetPasswordCodeFragment : BaseFragment<FragmentSendResetPasswordCodeBinding, ResetPasswordViewModel>() {
    override fun initViewModel(): ResetPasswordViewModel = viewModel<ResetPasswordViewModel>().value

    override fun getLayoutId(): Int = R.layout.fragment_send_reset_password_code

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.email.doOnTextChanged { email, _, _, _ ->
            if (!email.isNullOrEmpty()) {
                binding.emailField.error = null
                viewModel.updateEmail(email)
            }
        }

        binding.btnSendCode.setOnClickListener {
            if (binding.email.text.isNullOrEmpty()) {
                handleEmailError(ValidationError.EMPTY_EMAIL)
                return@setOnClickListener
            } else {
                viewModel.sendResetPasswordCodeWithEmail()
            }
        }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ResetPasswordEvent.Error -> {
                    event.authError?.let { error ->
                        val errorMsg =
                            when (error) {
                                AuthError.INVALID_EMAIL -> getString(R.string.error_invalid_email)
                                AuthError.USER_NOT_FOUND -> getString(R.string.error_user_not_found)
                                AuthError.OPERATION_NOT_ALLOWED -> getString(R.string.error_operation_not_allowed)
                                AuthError.USER_DISABLED -> getString(R.string.error_user_disabled)
                                AuthError.NETWORK_ERROR -> getString(R.string.error_network)
                                else -> getString(R.string.error_unexpected)
                            }

                        Toast
                            .makeText(
                                requireContext(),
                                errorMsg,
                                Toast.LENGTH_LONG,
                            ).show()
                    }

                    handleEmailError(event.emailValidationError)
                }

                ResetPasswordEvent.NavigateToConfirmResetPasswordCode -> {
                    findNavController()
                        .navigate(R.id.action_sendResetPasswordCodeFragment_to_confirmPasswordResetCodeFragment)
                }
            }
        }
    }

    private fun handleEmailError(emailError: ValidationError) {
        binding.emailField.error =
            when (emailError) {
                ValidationError.EMPTY_EMAIL -> getString(R.string.field_is_required)
                ValidationError.INVALID_EMAIL -> getString(R.string.invalid_email)
                else -> null
            }
    }
}
