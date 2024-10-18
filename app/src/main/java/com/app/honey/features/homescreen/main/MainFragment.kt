package com.app.honey.features.homescreen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.app.honey.basefragment.BaseFragmentVB
import com.app.honey.databinding.FragmentMainBinding
import com.app.honey.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragmentVB<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

//            backer.root.setOnClickListener { findNavController().popBackStack() }
        }

    }


    override fun getMyViewModel() = viewModel
}