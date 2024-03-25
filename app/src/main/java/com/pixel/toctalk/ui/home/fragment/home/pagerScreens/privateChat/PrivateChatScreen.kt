package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.privateChat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.databinding.FragmentPrivateChatBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.fragment.chat.MessageState
import com.pixel.toctalk.ui.home.fragment.home.HomeFragmentDirections
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.privateChat.adapter.ContactsAdapter

class PrivateChatScreen : BaseFragment<FragmentPrivateChatBinding, PrivateChatViewModel>() {
    override fun initViewModel(): PrivateChatViewModel =
        ViewModelProvider(this)[PrivateChatViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_private_chat

    private var _contactsAdapter: ContactsAdapter? = null
    private val contactsAdapter get() = _contactsAdapter!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContactsRecyclerOption()
        observeLiveData()
        initView()
    }

    private fun initView() {
        binding.lifecycleOwner = this
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { options ->
            _contactsAdapter = ContactsAdapter(requireContext(), options)
            initRecycler()
        }
    }

    private fun initRecycler() {
        binding.contactRecycler.adapter = contactsAdapter
        if (contactsAdapter.snapshots.isEmpty()) {
            binding.messageNoRooms.visibility = View.VISIBLE
        }
        binding.messageNoRooms.visibility = View.GONE
        contactsAdapter.setOnContactClickListener { contact ->
            navToContactChat(contact)
        }
        contactsAdapter.startListening()
    }

    private fun navToContactChat(contact: Contact) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToChatFragment(
                state = MessageState.CONTACT.value,
                contentChat = contact,
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _contactsAdapter = null
    }
}
