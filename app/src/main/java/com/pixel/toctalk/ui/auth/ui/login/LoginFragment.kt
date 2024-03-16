package com.pixel.toctalk.ui.auth.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.pixel.toctalk.Constants
import com.pixel.toctalk.R
import com.pixel.toctalk.data.database.model.User
import com.pixel.toctalk.databinding.FragmentLoginBinding
import com.pixel.toctalk.ui.auth.ui.InputState
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.extensions.showDialog

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun initViewModel(): LoginViewModel =
        ViewModelProvider(this)[LoginViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
        viewModel.checkLoggedIn()
    }

    private fun observeLiveData() {
        viewModel.messageDialog.observe(viewLifecycleOwner) {
            showDialog(it)
        }
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginViewEvent.NavigateToReg -> {
                    navigateToRegister(binding.root)
                }

                is LoginViewEvent.NavigateToHome -> {
                    val user = event.user
                    navigateToHome(user)
                }
            }
        }
    }

    private fun navigateToHome(user: User) {
        startActivity(
            Intent(
                requireContext(),
                com.pixel.toctalk.ui.home.MainActivity::class.java,
            ).putExtra(Constants.PARSE_USER, user),
        )
        requireActivity().finish()
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.logEmail.doOnTextChanged { email, _, _, _ ->
            viewModel.isValidInput(email, InputState.EmailInput)
        }
        binding.logPassword.doOnTextChanged { password, _, _, _ ->
            viewModel.isValidInput(password, InputState.PasswordInput)
        }
    }

    private fun navigateToRegister(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }
}
