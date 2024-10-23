package com.app.honey.features.homescreen.home.viewmodels

import ProductData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.honey.R
import com.app.honey.baseviewmodel.BaseViewModel
import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.data.productfragment.HoneyData
import com.app.honey.data.remote.model.data.productfragment.Product
import com.app.honey.data.remote.model.data.productfragment.Review
import com.app.honey.data.remote.model.data.productfragment.SummaryData
import com.app.honey.data.remote.model.response.configuration.ConfigData
import com.app.honey.domain.model.ErrorModel
import com.app.honey.domain.repo.CacheRepo
import com.app.honey.domain.repo.SplashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepo: SplashRepo,
    private val cacheRepo: CacheRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // StateFlow to hold the products list
    private val _configurations = MutableStateFlow<ConfigData?>(null)
    val configurations: StateFlow<ConfigData?> = _configurations

    init {
        // Simulate loading data
    }

    fun getConfigurations() {
        viewModelScope.launch {
            showLoader()
            when (val call = splashRepo.getConfigurations()) {
                is Resource.Error -> {
                    showError(ErrorModel(title = "Error", message = call.error))
                }
                is Resource.Success -> {
                    hideLoader()
                    if (call.data.responseCode == 200) {
                        _configurations.value = call.data.data
                    } else {
                        showError(ErrorModel("Error", call.data.message.toString()))
                    }
                }
            }
        }
    }
}