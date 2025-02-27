package com.pixel.toctalk.auth.presentation.fragment.resetPassword.confirmCode

import com.pixel.toctalk.R
import com.pixel.toctalk.auth.presentation.fragment.resetPassword.ResetPasswordViewModel
import com.pixel.toctalk.core.presentation.BaseFragment
import com.pixel.toctalk.databinding.FragmentConfirmPasswordResetCodeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmPasswordResetCodeFragment : BaseFragment<FragmentConfirmPasswordResetCodeBinding, ResetPasswordViewModel>() {
    override fun initViewModel(): ResetPasswordViewModel = viewModel<ResetPasswordViewModel>().value

    override fun getLayoutId(): Int = R.layout.fragment_confirm_password_reset_code
}
