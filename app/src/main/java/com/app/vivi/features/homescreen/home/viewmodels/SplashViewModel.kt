package com.app.vivi.features.homescreen.home.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.configuration.AppConfigResponse
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.SplashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepo: SplashRepo,
    private val cacheRepo: CacheRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _channel = Channel<NavigationEvents>()
    val channel = _channel.receiveAsFlow()

    init {
        // Simulate loading data
    }

    fun getConfigurations() {
        viewModelScope.launch {
            showLoader()
            when (val call = splashRepo.getConfigurations()) {
                is Resource.Error -> {
                    showError(ErrorModel(title = call.title, message = call.message, call.code))
                }

                is Resource.Success -> {
                    hideLoader()

                    cacheRepo.saveConfigurationData(call.data)
                    _channel.send(NavigationEvents.NavigateToMainScreen(call.data))
                    /*if (call.data.responseCode == 200) {
                        cacheRepo.saveConfigurationData(call.data.data)
                        _channel.send(NavigationEvents.NavigateToMainScreen(call.data))
                    } else {
                        showError(ErrorModel("Error", call.data.message.toString()))
                    }*/
                }
            }
        }
    }

    sealed class NavigationEvents {
        data class NavigateToMainScreen(val response: AppConfigResponse) : NavigationEvents()
    }
}