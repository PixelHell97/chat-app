package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.allGroups

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.FragmentBrowseGroupsBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.fragment.chat.MessageState
import com.pixel.toctalk.ui.home.fragment.home.HomeFragmentDirections
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.adapter.GroupRecyclerAdapter

class BrowseGroupsScreen : BaseFragment<FragmentBrowseGroupsBinding, BrowseGroupsViewModel>() {
    override fun initViewModel(): BrowseGroupsViewModel =
        ViewModelProvider(this)[BrowseGroupsViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_browse_groups

    private var _allGroupsAdapter: GroupRecyclerAdapter? = null
    private val allGroupsAdapter get() = _allGroupsAdapter!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initGroupsRecyclerOption()
        observeLiveData()
    }

    private fun initRecyclerView() {
        binding.browseGroupsRv.adapter = allGroupsAdapter
        allGroupsAdapter.setOnGroupClickListener { group ->
            val user = Firebase.auth.currentUser ?: return@setOnGroupClickListener
            if (viewModel.isGroupMember(user.uid, group)) {
                navToChat(group)
            } else {
                showJoinBottomSheet(group)
            }
        }
        if (allGroupsAdapter.snapshots.isEmpty()) {
            binding.messageNoRooms.visibility = View.VISIBLE
        } else {
            binding.messageNoRooms.visibility = View.GONE
        }
        allGroupsAdapter.startListening()
    }

    private fun showJoinBottomSheet(group: Group) {
        val action =
            HomeFragmentDirections.actionNavHomeToJoinGroupBottomSheet(group)
        findNavController().navigate(action)
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { option ->
            _allGroupsAdapter = GroupRecyclerAdapter(option)
            initRecyclerView()
        }
    }

    private fun navToChat(group: Group) {
        val action =
            HomeFragmentDirections
                .actionHomeFragmentToChatFragment(
                    state = MessageState.GROUP.value,
                    groupChat = group,
                )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _allGroupsAdapter = null
    }
}
