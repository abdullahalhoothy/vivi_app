package com.app.honey.entrypoint

import androidx.lifecycle.viewModelScope
import com.app.honey.baseviewmodel.BaseViewModel
import com.app.honey.data.remote.model.response.LoginResponse
import com.app.honey.domain.repo.CacheRepo
import com.app.honey.entrypoint.model.DrawerItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val cacheRepo: CacheRepo) :
    BaseViewModel() {

    var keepSplashScreen: Boolean = true

    private val _drawerItems = MutableStateFlow(emptyList<DrawerItemModel>())
    val drawerItems = _drawerItems.asStateFlow()

    private val _channel = Channel<NavigationEvents>()
    val channel = _channel.receiveAsFlow()

    init {
        /*viewModelScope.launch {
            _drawerItems.emit(
                listOf(
                    DrawerItemModel("Profile"),
                    DrawerItemModel("Schedule"),
                    DrawerItemModel("Urgent Call Request"),
                    DrawerItemModel("On Call Group"),
                    DrawerItemModel("ePrescription"),
                    DrawerItemModel("SOAP Note"),
                    DrawerItemModel("Medical Records"),
                    DrawerItemModel("E-Visit Free"),
                    DrawerItemModel("Invite People"),
                )
            )
        }*/

        viewModelScope.launch {
            keepSplashScreen = true
            if (isUserLoggedIn()) {
                cacheRepo.getLoginResponse().firstOrNull()?.let { loginResponse ->
                    cacheRepo.saveDoctorEmail(loginResponse.data.email.orEmpty())
                    _channel.send(NavigationEvents.NavigateToMainScreen(loginResponse))
                    //this delay is placed to prevent a flicker of login screen if user is already logged in
                    delay(500)
                }
            }
            keepSplashScreen = false
        }
    }

    private suspend fun isUserLoggedIn(): Boolean {
        return cacheRepo.isLoggedIn().first()
    }

    sealed class NavigationEvents {
        data class NavigateToMainScreen(val loginResponse: LoginResponse) : NavigationEvents()
    }

}