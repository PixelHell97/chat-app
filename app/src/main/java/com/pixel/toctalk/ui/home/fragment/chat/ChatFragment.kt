package com.pixel.toctalk.ui.home.fragment.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.FragmentChatBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.MainActivity
import com.pixel.toctalk.ui.home.fragment.chat.adapter.ChatRecyclerView

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    override fun initViewModel(): ChatViewModel =
        ViewModelProvider(this)[ChatViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_chat

    private val args: ChatFragmentArgs by navArgs()

    private var _chatAdapter: ChatRecyclerView? = null
    private val chatAdapter get() = _chatAdapter!!

    private var _state: MessageState? = null
    private val state get() = _state!!

    /*private var _toolbar: MaterialToolbar? = null
    private val toolbar get() = _toolbar!!*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (args.state) {
            MessageState.CONTACT.value -> {
                // TODO: (activity as MainActivity).supportActionBar?.title = args.contentChat?.username
                initPrivateChatView()
                viewModel.messageState.value = MessageState.CONTACT
                viewModel.initContactChatFireStoreOption()
                _state = MessageState.CONTACT
            }

            MessageState.GROUP.value -> {
                (activity as MainActivity).supportActionBar?.title = args.groupChat?.name
                initGroupView()
                viewModel.messageState.value = MessageState.GROUP
                viewModel.initGroupChatFireStoreOption()
                _state = MessageState.GROUP
            }
        }
        observeLiveData()
    }

    private fun initPrivateChatView() {
        val contact = args.contentChat
        viewModel.contact.value = contact
        binding.vm = viewModel
        binding.lifecycleOwner = this
        /*_toolbar = requireActivity().findViewById(R.id.home_toolbar)
        toolbar.let {
            it.setOnClickListener {
                navToContactDetails(contact)
            }
        }*/
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { option ->
            _chatAdapter = ChatRecyclerView(state, option)
            initRecycler()
        }
    }

    private fun initRecycler() {
        val linearLayout = LinearLayoutManager(requireContext())
        linearLayout.reverseLayout = true
        binding.messageRecyclerView.layoutManager = linearLayout
        binding.messageRecyclerView.adapter = chatAdapter
        chatAdapter.startListening()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.messageRecyclerView.smoothScrollToPosition(0)
            }
        })
    }

    private fun initGroupView() {
        val group = args.groupChat
        viewModel.group.value = group
        binding.vm = viewModel
        binding.lifecycleOwner = this
        /*_toolbar = requireActivity().findViewById(R.id.home_toolbar)
        toolbar.let {
            it.setOnClickListener {
                navToGroupDetails(group)
            }
        }*/
    }

    private fun navToGroupDetails(group: Group?) {
        val action =
            ChatFragmentDirections.actionChatFragmentToGroupDetailsFragment(group!!)
        findNavController().navigate(action)
    }

    private fun navToContactDetails(contact: Contact?) {
        val action =
            ChatFragmentDirections.actionChatFragmentToContentDetailsFragment(contact!!)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _chatAdapter = null
        _state = null
    }
}
