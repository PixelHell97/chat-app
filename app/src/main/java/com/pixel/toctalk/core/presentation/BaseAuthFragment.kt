package com.pixel.toctalk.core.presentation

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.pixel.toctalk.R
import com.pixel.toctalk.app.MainActivity
import com.pixel.toctalk.core.Constants
import com.pixel.toctalk.core.domain.util.AuthError

abstract class BaseAuthFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment<VB, VM>() {
    fun navigateToHome(userId: String?) {
        startActivity(
            Intent(
                requireContext(),
                MainActivity::class.java,
            ).putExtra(Constants.PARSE_USER, userId),
        )
        requireActivity().finish()
    }

    fun handleAuthError(error: AuthError) {
        val errorMsg =
            when (error) {
                AuthError.INVALID_EMAIL -> getString(R.string.error_invalid_email)
                AuthError.EMAIL_ALREADY_IN_USE -> getString(R.string.error_email_already_in_use)
                AuthError.WRONG_EMAIL_OR_PASSWORD -> getString(R.string.error_wrong_email_or_password)
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
}
