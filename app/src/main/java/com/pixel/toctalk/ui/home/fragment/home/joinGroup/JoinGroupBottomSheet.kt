package com.pixel.toctalk.ui.home.fragment.home.joinGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.BottomSheetJoinGroupBinding
import com.pixel.toctalk.ui.home.fragment.chat.MessageState

class JoinGroupBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetJoinGroupBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: JoinGroupBottomSheetViewModel
    private val args: JoinGroupBottomSheetArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[JoinGroupBottomSheetViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetJoinGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val group = args.group
        binding.group = group
        binding.btnJoin.setOnClickListener {
            viewModel.addMemberToGroup(group)
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is JoinGroupState.NavigateToGroupChat -> {
                    navToChat(state.group)
                }
            }
        }
    }

    private fun navToChat(group: Group) {
        val action =
            JoinGroupBottomSheetDirections.actionJoinGroupBottomSheetToChatFragment(
                state = MessageState.GROUP.value,
                groupChat = group,
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
