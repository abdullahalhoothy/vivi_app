package com.app.vivi.features.homescreen.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.CustomTabBinding
import com.app.vivi.databinding.FragmentHomeLatestBinding
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.features.homescreen.home.adapters.ViewPagerAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragmentLatest :
    BaseFragmentVB<FragmentHomeLatestBinding>(FragmentHomeLatestBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    override fun getMyViewModel() = viewModel


    /*private val mDrawerAdapter by lazy { DrawerAdapter() }
    private var ed: androidx.appcompat.app.AlertDialog? = null

    private val mAdapter by lazy {
        HomeListAdapter(
            onItemClick = {
                viewModel.onPatientClick(it)
                *//*collectWhenStarted {
                    ed = errorDialog(ErrorModel("Error", "Message")) {
                        ed?.dismiss()
                    }
                    ed?.safeShow()
                    ed?.safeShow()
                }*//*
            },
            onEditClick = { viewModel.onEditPatientClick(it) },
            onDeleteClick = { viewModel.onDeletePatientClick(it) }
        )
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initListeners()

        setFragmentResultListener("refresh") { _, bundle ->
            val refresh = bundle.getBoolean("refresh")
            if (refresh) {
//                viewModel.refreshPatientList()
            }
        }
    }

    private fun initListeners(){
        binding.apply {
            centerImageView.setOnClickListener {
                val action = HomeFragmentLatestDirections.actionLatestHomeFragmentToNotificationsFragment()
                findNavController().navigateWithSingleTop(action)
            }

            fabCamera.setOnClickListener {
                val action = HomeFragmentLatestDirections.actionLatestHomeFragmentToScannerFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initViewPager() {
        // Set up ViewPager with FragmentAdapter
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        /* // Link TabLayout with ViewPager
         TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
             when (position) {
                 0 -> tab.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.menu_pharmacy)
                 1 -> tab.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.search)
                 2 -> tab.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_date_picker)
                 3 -> tab.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.btn_selected)
             }
         }.attach()*/


        // Tab Icons
        val tabIcons = arrayOf(
            R.drawable.ic_home,  // Home tab icon
            R.drawable.ic_search_latest,  // Search tab icon
            R.drawable.ic_social,  // Social tab icon
            R.drawable.ic_profile  // Profile tab icon
        )

        // Link TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = getTabView(position, tabIcons)
        }.attach()

        // Set default tab (first tab as selected)
        selectTab(binding.tabLayout.getTabAt(0), true)

        // Update Tab Icons based on selection
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until binding.tabLayout.tabCount) {
                    val tab = binding.tabLayout.getTabAt(i)
                    selectTab(tab, i == position)
                }
            }
        })
    }

    // Inflate custom tab layout and set icon
    private fun getTabView(position: Int, icons: Array<Int>): View {
        val binding = CustomTabBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tabIcon.setImageResource(icons[position])
        return binding.root
    }

    // Change tab icon tint color based on selection
    private fun selectTab(tab: TabLayout.Tab?, isSelected: Boolean) {
        val icon = tab?.customView?.findViewById<ImageView>(R.id.tabIcon)
        if (isSelected) {
            // Set selected tint color
            icon?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.tab_selected_color
                )
            )
        } else {
            // Set unselected tint color
            icon?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.tab_unselected_color
                )
            )
        }
    }


}