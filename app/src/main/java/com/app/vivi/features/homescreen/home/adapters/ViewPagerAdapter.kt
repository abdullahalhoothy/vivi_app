package com.app.vivi.features.homescreen.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.vivi.features.homescreen.home.fragments.ProductFragment
import com.app.vivi.features.homescreen.home.fragments.ProfileFragment
import com.app.vivi.features.homescreen.home.fragments.SearchFragment
import com.app.vivi.features.homescreen.home.fragments.SocialFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4 // 4 tabs: Home, Search, Social, Profile

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductFragment()
            1 -> SearchFragment()
            2 -> SocialFragment()
            else -> ProfileFragment()
        }
    }
}
