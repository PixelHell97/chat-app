package com.pixel.toctalk.auth.presentation.fragment.createAccount.completeProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pixel.toctalk.R
import com.pixel.toctalk.auth.domain.util.ValidationError
import com.pixel.toctalk.auth.presentation.fragment.createAccount.CreateAccountAction
import com.pixel.toctalk.auth.presentation.fragment.createAccount.RegisterViewEvent
import com.pixel.toctalk.auth.presentation.fragment.createAccount.RegisterViewModel
import com.pixel.toctalk.core.permission.RequestMediaPermission
import com.pixel.toctalk.core.presentation.BaseAuthFragment
import com.pixel.toctalk.databinding.FragmentCompleteOfEditAccountBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompleteProfileFragment : BaseAuthFragment<FragmentCompleteOfEditAccountBinding, RegisterViewModel>() {
    private var _startForProfileImageResult: ActivityResultLauncher<Intent>? = null
    private val startActivityForResult get() = _startForProfileImageResult!!

    private var imageUri: Uri? = null

    override fun initViewModel(): RegisterViewModel = viewModel<RegisterViewModel>().value

    override fun getLayoutId(): Int = R.layout.fragment_complete_of_edit_account

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initStartActivityForResult()
        initViews()
        observeData()
    }

    private fun initStartActivityForResult() {
        _startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        imageUri = data?.data!!
                        binding.openPic.setImageURI(imageUri)
                        // viewModel.setProfilePic(imageUri)
                    }

                    ImagePicker.RESULT_ERROR -> {
                        Toast
                            .makeText(
                                requireContext(),
                                ImagePicker.getError(data),
                                Toast.LENGTH_SHORT,
                            ).show()
                    }

                    else -> {
                        Toast
                            .makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is RegisterViewEvent.Navigate -> navigateToHome(event.uid)
                is RegisterViewEvent.Error -> {
                    event.authError?.let {
                        handleAuthError(event.authError)
                    }
                    handleUsernameError(event.nameValidationError)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest { state ->
                    binding.isLoading = state.isLoading
                }
            }
        }
    }

    private fun initViews() {
        binding.changePic.setOnClickListener {
            RequestMediaPermission.requestGalleryPermission(
                requireActivity(),
                this,
                startActivityForResult,
            )
        }
        binding.btnSaveChanges.setOnClickListener {
            val username =
                binding.username.text
                    ?.toString()
                    .orEmpty()

            if (username.isEmpty()) {
                handleUsernameError(ValidationError.EMPTY_USERNAME)
                binding.username.requestFocus()
                return@setOnClickListener
            }
            viewModel.handleAction(
                CreateAccountAction.UpdateUserData(
                    username = username,
                    userImage = imageUri,
                ),
            )
        }
    }

    private fun handleUsernameError(usernameError: ValidationError) {
        binding.usernameField.error =
            when (usernameError) {
                ValidationError.EMPTY_USERNAME -> getString(R.string.field_is_required)
                ValidationError.USERNAME_OUT_OF_RANGE -> getString(R.string.username_out_of_range)
                else -> null
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _startForProfileImageResult = null
    }
}
