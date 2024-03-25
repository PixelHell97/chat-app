package com.pixel.toctalk.ui.home.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.allGroups.BrowseGroupsScreen
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.myGroups.GroupsScreen
import com.pixel.toctalk.ui.home.fragment.home.pagerScreens.privateChat.PrivateChatScreen

class HomeViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = arrayListOf(
        PrivateChatScreen(),
        GroupsScreen(),
        BrowseGroupsScreen(),
    )

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}
