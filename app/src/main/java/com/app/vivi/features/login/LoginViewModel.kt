package com.app.vivi.features.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.request.ResetPasswordRequest
import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse.FilteredProduct
import com.app.vivi.data.remote.model.response.login.ResetPasswordResponse
import com.app.vivi.domain.interactors.EmailValidationUseCase
import com.app.vivi.domain.interactors.PasswordValidationUseCase
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    private val cacheRepo: CacheRepo,
    private val emailValidationUseCase: EmailValidationUseCase,
    private val passwordValidationUseCase: PasswordValidationUseCase
) : BaseViewModel() {

    private val _emailErrorFlow = MutableStateFlow("")
    val emailErrorFlow = _emailErrorFlow.asStateFlow()

    private val _passwordErrorFlow = MutableStateFlow("")
    val passwordErrorFlow = _passwordErrorFlow.asStateFlow()

    private val _channel = Channel<NavigationEvents>()
    val channel = _channel.receiveAsFlow()

    private val _channelLoginEmail = Channel<NavigationLoginEmailEvents>()
    val channelLoginEmail = _channelLoginEmail.receiveAsFlow()

    // StateFlow to hold the products list
    private val _resetPasswordObserver = MutableStateFlow<ResetPasswordResponse?>(null)
    val resetPasswordObserver: StateFlow<ResetPasswordResponse?> = _resetPasswordObserver

    fun onLoginContinueClicked(email: String) {
        viewModelScope.launch {
            if (emailValidationUseCase.isValid(email)) {
                _emailErrorFlow.emit("")
            } else {
                _emailErrorFlow.emit("Valid Email is required")
                return@launch
            }

//            _channelLoginEmail.send(NavigationLoginEmailEvents.NavigateToLoginScreen(""))

            showLoader()
            viewModelScope.launch {
                if (emailValidationUseCase.isValid(email)) {
                    _emailErrorFlow.emit("")
                } else {
                    _emailErrorFlow.emit("Valid Email is required")
                    return@launch
                }
                showLoader()
                when (val call = loginRepo.getResetPasswordApi(ResetPasswordRequest(email))) {
                    is Resource.Error -> {
                        if (call.code == 401) {
                            showError(ErrorModel(title = call.title, message = call.message, call.code))
                        } else {
                            showError(ErrorModel(title = call.title, message = call.message, call.code))
                        }
                    }

                    is Resource.Success -> {
                        hideLoader()
                        _resetPasswordObserver.value = call.data
                    }
                }
            }
        }
    }

    fun onLoginClicked(email: String, password: String) {
        viewModelScope.launch {
            if (emailValidationUseCase.isValid(email)) {
                _emailErrorFlow.emit("")
            } else {
                _emailErrorFlow.emit("Valid Email is required")
                return@launch
            }

            if (passwordValidationUseCase.isValid(password)) {
                _passwordErrorFlow.emit("")
            } else {
                _passwordErrorFlow.emit("Password is required")
                return@launch
            }
//            _channel.send(NavigationEvents.NavigateToMainScreen())
            showLoader()
            when (val call = loginRepo.login(
                loginRequest = LoginRequest(email, password)
            )) {
                is Resource.Error -> {
                    hideLoader()
                    if (call.code == 401) {
                        call.message.let { errorBody ->
                            try {
                                // Convert ResponseBody to a JSON string
                                val errorBodyString = errorBody.toString()

                                // Check if the errorBodyString is empty
                                if (errorBodyString.isNotEmpty()) {
                                    // Parse the string into a JSONObject
                                    val json = JSONObject(errorBodyString)

                                    // Extract the "detail" field
                                    val detailMessage = json.getString("detail")
                                    showError(
                                        ErrorModel(
                                            title = call.title,
                                            message = detailMessage,
                                            call.code
                                        )
                                    )
                                } else {
                                    println("Error body is empty.")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                println("Failed to parse error body.")
                            }
                        } ?: run {
                            println("Error body is null")
                        }
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    cacheRepo.setLoggedIn(true)
                    cacheRepo.saveLoginResponse(call.data)
                    _channel.send(NavigationEvents.NavigateToMainScreen(call.data))
                }
            }
        }
    }

    fun onEmailTextChanged(text: String) {
        viewModelScope.launch {
            if (emailValidationUseCase.isValid(text)) {
                _emailErrorFlow.emit("")
            }
        }
    }

    fun onPasswordChanged(text: String) {
        viewModelScope.launch {
            if (passwordValidationUseCase.isValid(text)) {
                _passwordErrorFlow.emit("")
            }
        }
    }



    fun getResetPasswordApi(email: String) {

    }

    sealed class NavigationEvents {
        data class NavigateToMainScreen(val loginResponse: LoginResponse? = null) :
            NavigationEvents()
    }

    sealed class NavigationLoginEmailEvents {
        data class NavigateToLoginScreen(val login: String) : NavigationLoginEmailEvents()
    }
}