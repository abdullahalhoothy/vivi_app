package com.app.honey.features.homescreen.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.app.honey.basefragment.BaseFragmentVB
import com.app.honey.databinding.FragmentMainBinding
import com.app.honey.databinding.FragmentProductBinding
import com.app.honey.databinding.FragmentSearchBinding
import com.app.honey.databinding.FragmentSocialBinding
import com.app.honey.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment :
    BaseFragmentVB<FragmentSocialBinding>(FragmentSocialBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            /*rvMenu.layoutManager = GridLayoutManager(requireContext(), 2)
            rvMenu.adapter = mAdapter

            backer.root.setOnClickListener { findNavController().popBackStack() }*/
        }

    }


    override fun getMyViewModel() = viewModel
}