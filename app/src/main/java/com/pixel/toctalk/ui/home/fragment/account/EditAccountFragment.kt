package com.pixel.toctalk.ui.home.fragment.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.data.database.UserMdb
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.data.utils.FirebaseUtils
import com.pixel.toctalk.databinding.FragmentEditAccountBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.extensions.showDialog
import com.pixel.toctalk.ui.home.MainActivity
import com.pixel.toctalk.ui.permission.RequestMediaPermission
import kotlinx.coroutines.launch

class EditAccountFragment : BaseFragment<FragmentEditAccountBinding, EditAccountViewModel>() {
    override fun initViewModel(): EditAccountViewModel =
        ViewModelProvider(this)[EditAccountViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_edit_account

    private lateinit var imagePicker: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            viewModel.setProfilePic(it)
        }
        getMyUser()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                EditAccountEvent.SUCCESSFUL -> {
                    Toast.makeText(
                        requireContext(),
                        "Account is successfully updated",
                        Toast.LENGTH_LONG,
                    ).show()
                    restartApp()
                }

                EditAccountEvent.UNSUCCESSFUL -> {
                    showDialog(
                        requireActivity().resources.getString(R.string.update_account_failed),
                        requireActivity().resources.getString(R.string.back),
                        {
                            findNavController().popBackStack()
                        },
                    )
                }

                null -> {}
            }
        }
    }

    private fun restartApp() {
        startActivity(
            Intent(
                requireActivity(),
                MainActivity::class.java,
            ),
        )
        requireActivity().finish()
    }

    private fun getMyUser() {
        lifecycleScope.launch {
            UserMdb.getUser(FirebaseUtils.getCurrentUserID()) {
                if (it.isSuccessful) {
                    val myUser = it.result.toObject(User::class.java)
                    initView(myUser!!)
                }
            }
        }
    }

    private fun initView(myUser: User) {
        viewModel.user.value = myUser
        viewModel.userCurrentPic.value = myUser.profilePic
        viewModel.usernameLiveData.value = myUser.username
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.username.doOnTextChanged { username, _, _, _ ->
            viewModel.isValidInput(username)
        }
        binding.changePic.setOnClickListener {
            RequestMediaPermission.requestGalleryPermission(requireActivity(), this, imagePicker)
        }
    }
}
