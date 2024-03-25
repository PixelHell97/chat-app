package com.pixel.toctalk.ui.home.fragment.createGroup

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.FragmentCreateGroupBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.permission.RequestMediaPermission

class CreateGroupFragment : BaseFragment<FragmentCreateGroupBinding, CreateGroupViewModel>() {
    override fun initViewModel(): CreateGroupViewModel =
        ViewModelProvider(this)[CreateGroupViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_create_group

    private lateinit var imagePicker: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.setGroupPic(it)
            }
        }
        initView()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CreateGroupStates.GroupCreated -> {
                    navigateToChat(state.group)
                }
            }
        }
    }

    private fun navigateToChat(group: Group) {
        val action =
            CreateGroupFragmentDirections.actionCreateGroupFragmentToChatFragment(
                groupChat = group,
                state = 1,
                contentChat = Contact(),
            )
        findNavController().navigate(action)
        // TODO: How to remove this fragment after create the group
    }

    private fun initView() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.nameEd.doOnTextChanged { name, _, _, _ ->
            viewModel.isValidGroupName(name)
        }
        binding.groupImage.setOnClickListener {
            RequestMediaPermission.requestGalleryPermission(requireActivity(), imagePicker)
        }
    }
}
