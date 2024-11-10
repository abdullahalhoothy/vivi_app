package com.app.vivi.features.homescreen.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentSearchBinding
import com.app.vivi.features.homescreen.home.adapters.search.OuterAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    BaseFragmentVB<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initRecyclerview()

    }

    private fun initViews(){
        binding.apply {
            tvShopByType.text = getString(R.string.shop_by_type_txt, getString(R.string.app_name))
        }
    }

    private fun initRecyclerview(){
//        val outerRecyclerView: RecyclerView = findViewById(R.id.outerRecyclerView)

        // Example data
        val data = initializeItems()

        val outerAdapter = OuterAdapter()
        binding.rvOuter.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvOuter.adapter = outerAdapter

        outerAdapter.submitList(data)
    }

    private fun initializeItems(): List<List<String>> {
        return listOf(
            listOf("Espresso", "Double Espresso", "Ristretto"),
            listOf("Latte", "Cappuccino", "Flat White"),
            listOf("Cold Brew", "Iced Coffee", "Nitro Cold Brew"),
            listOf("Americano", "Long Black", "Red Eye"),
            listOf("Mocha", "White Mocha", "Dark Mocha"))
    }

    private var items: List<List<String>> = initializeItems()



    override fun getMyViewModel() = viewModel
}