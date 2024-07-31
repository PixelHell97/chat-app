package com.pixel.toctalk.ui.home.fragment.home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.pixel.toctalk.R
import com.pixel.toctalk.databinding.FragmentHomeBinding
import com.pixel.toctalk.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun initViewModel(): HomeViewModel =
        ViewModelProvider(this)[HomeViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_home

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim,
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim,
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_amin,
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim,
        )
    }
    private var isClicked = false

    private lateinit var viewPagerAdapter: HomeViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initView()
    }

    private fun initView() {
        binding.openFabMenu.setOnClickListener {
            onOpenFabClick()
        }
        binding.addContactFAB.setOnClickListener {
            navToAddContent()
        }
        binding.createGroupFAB.setOnClickListener {
            navToGroupCreation()
        }
    }

    private fun navToAddContent() {
        findNavController()
            .navigate(R.id.action_nav_home_to_searchContactBottomSheet)
    }

    private fun navToGroupCreation() {
        findNavController()
            .navigate(R.id.action_homeFragment_to_createGroupFragment)
    }

    private fun onOpenFabClick() {
        setVisibility(isClicked)
        setAnimation(isClicked)
        setClickable(isClicked)
        isClicked = !isClicked
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.addContactFAB.isClickable = true
            binding.createGroupFAB.isClickable = true
        } else {
            binding.addContactFAB.isClickable = false
            binding.createGroupFAB.isClickable = false
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.openFabMenu.startAnimation(rotateOpen)
            binding.addContactFAB.startAnimation(fromBottom)
            binding.createGroupFAB.startAnimation(fromBottom)
        } else {
            binding.openFabMenu.startAnimation(rotateClose)
            binding.addContactFAB.startAnimation(toBottom)
            binding.createGroupFAB.startAnimation(toBottom)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.addContactFAB.visibility = View.VISIBLE
            binding.createGroupFAB.visibility = View.VISIBLE
        } else {
            binding.addContactFAB.visibility = View.INVISIBLE
            binding.createGroupFAB.visibility = View.INVISIBLE
        }
    }

    private fun initViewPager() {
        /*val fragmentList = arrayListOf(
            PrivateChatScreen(),
            GroupsScreen(),
            BrowseGroupsScreen(),
        )*/
        viewPagerAdapter =
            HomeViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.homeViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            val tabTitle = resources.getStringArray(R.array.home_tabs_title)
            tab.text = tabTitle[position]
        }.attach()
    }
}
