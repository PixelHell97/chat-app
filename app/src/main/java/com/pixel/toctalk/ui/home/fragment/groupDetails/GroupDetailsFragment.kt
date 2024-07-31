package com.pixel.toctalk.ui.home.fragment.groupDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.pixel.toctalk.R
import com.pixel.toctalk.databinding.FragmentGroupDetailsBinding
import com.pixel.toctalk.ui.base.BaseFragment
import com.pixel.toctalk.ui.home.MainActivity
import com.pixel.toctalk.ui.home.fragment.adapter.MembersAdapter

class GroupDetailsFragment : BaseFragment<FragmentGroupDetailsBinding, GroupDetailsViewModel>() {
    override fun initViewModel(): GroupDetailsViewModel =
        ViewModelProvider(this)[GroupDetailsViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_group_details

    private val args: GroupDetailsFragmentArgs by navArgs()
    private lateinit var adapter: MembersAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroupMembersOption(args.group)
        (activity as MainActivity).supportActionBar?.also {
            it.title = args.group.name
        }
        initView()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.option.observe(viewLifecycleOwner) { option ->
            adapter = MembersAdapter(option)
            initRecycler()
        }
    }

    private fun initRecycler() {
        binding.membersRv.adapter = adapter
        adapter.startListening()
    }

    private fun initView() {
        binding.group = args.group
        binding.lifecycleOwner = this
    }
}
