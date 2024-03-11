package com.pixel.toctalk.auth.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.extensions.showDialog
import com.pixel.toctalk.auth.ui.InputState
import com.pixel.toctalk.base.BaseFragment
import com.pixel.toctalk.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    override fun initViewModel(): RegisterViewModel =
        ViewModelProvider(this)[RegisterViewModel::class.java]

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
        viewModel.messageDialog.observe(viewLifecycleOwner) {
            showDialog(it)
        }
        viewModel.event.observe(viewLifecycleOwner, ::onEventChange)
    }

    private fun onEventChange(event: RegisterViewEvent) {
        when (event) {
            is RegisterViewEvent.NavigateToLogin -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.regUsername.doOnTextChanged { username, _, _, _ ->
            viewModel.isValidInput(username, InputState.UsernameInput)
        }
        binding.regEmail.doOnTextChanged { email, _, _, _ ->
            viewModel.isValidInput(email, InputState.EmailInput)
        }
        binding.regPassword.doOnTextChanged { password, _, _, _ ->
            viewModel.isValidInput(password, InputState.PasswordInput)
        }
    }
}
