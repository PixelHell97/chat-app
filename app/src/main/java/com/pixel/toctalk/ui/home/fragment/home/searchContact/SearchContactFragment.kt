package com.pixel.toctalk.ui.home.fragment.home.searchContact

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.databinding.FragmentSearchContactBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.fragment.adapter.MembersAdapter
import com.pixel.toctalk.ui.home.fragment.chat.MessageState

class SearchContactFragment : BaseFragment<FragmentSearchContactBinding, SearchContactViewModel>() {
    override fun initViewModel(): SearchContactViewModel =
        ViewModelProvider(this)[SearchContactViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_search_contact

    private lateinit var adapter: MembersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { option ->
            adapter = MembersAdapter(option)
            initRecyclerView()
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchContactState.NavToChat -> {
                    navToChat(state.contact)
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.contactsRv.adapter = adapter
        if (adapter.snapshots.isEmpty()) {
            binding.messageNoUserFound.visibility = View.VISIBLE
        } else {
            binding.messageNoUserFound.visibility = View.GONE
        }
        adapter.setOnContactClickListener { user ->
            viewModel.createChatInDB(user)
        }
        adapter.startListening()
    }

    private fun navToChat(contact: Contact) {
        val action =
            SearchContactFragmentDirections.actionSearchContactBottomSheetToChatFragment(
                state = MessageState.CONTACT.value,
                contentChat = contact,
            )
        findNavController().navigate(action)
    }
}
