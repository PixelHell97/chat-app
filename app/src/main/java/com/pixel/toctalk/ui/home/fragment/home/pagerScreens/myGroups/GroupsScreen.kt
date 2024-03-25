package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.myGroups

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.FragmentGroupsBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.fragment.chat.MessageState
import com.pixel.toctalk.ui.home.fragment.home.HomeFragmentDirections
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.adapter.GroupRecyclerAdapter

class GroupsScreen : BaseFragment<FragmentGroupsBinding, GroupsViewModel>() {
    override fun initViewModel(): GroupsViewModel =
        ViewModelProvider(this)[GroupsViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_groups

    private var _myGroupAdapter: GroupRecyclerAdapter? = null
    private val myGroupAdapter get() = _myGroupAdapter!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initGroupsRecyclerOption(Firebase.auth.currentUser?.uid!!)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { option ->
            _myGroupAdapter = GroupRecyclerAdapter(option)
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        binding.myGroups.adapter = myGroupAdapter
        myGroupAdapter.setOnGroupClickListener { group ->
            navToChat(group)
        }
        if (myGroupAdapter.snapshots.isEmpty()) {
            binding.messageNoRooms.visibility = View.VISIBLE
        } else {
            binding.messageNoRooms.visibility = View.GONE
        }
        myGroupAdapter.startListening()
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
        _myGroupAdapter = null
    }
}
