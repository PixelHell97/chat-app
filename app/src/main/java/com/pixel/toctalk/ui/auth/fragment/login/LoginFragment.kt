package com.pixel.toctalk.ui.auth.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.Constants
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.databinding.FragmentLoginBinding
import com.pixel.toctalk.ui.auth.fragment.InputState
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.extensions.showDialog
import com.pixel.toctalk.ui.home.MainActivity

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
    }

    private fun observeLiveData() {
        viewModel.messageDialogModelDialog.observe(viewLifecycleOwner) {
            showDialog(it)
        }
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
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
                MainActivity::class.java,
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
        binding.createAccount.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        findNavController()
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }
}
