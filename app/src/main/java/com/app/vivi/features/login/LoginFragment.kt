package com.app.vivi.features.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginBinding
import com.app.vivi.extension.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class LoginFragment : BaseFragmentVB<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.onLoginClicked(
                    tieEmail.text.toString(),
                    "d"
                )
            }

            tieEmail.doOnTextChanged { text, start, before, count ->
                viewModel.onEmailTextChanged(text.toString())
            }

            handleBackPress()

            /*tiePassword.doOnTextChanged { text, start, before, count ->
                viewModel.onPasswordChanged(text.toString())
            }*/
        }

        addObservers()
    }


    private fun handleBackPress() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {
        collectWhenStarted {
            viewModel.emailErrorFlow.collectLatest {
                binding.tilEmail.error = it
            }
        }

        collectWhenStarted {
            viewModel.passwordErrorFlow.collectLatest {
//                binding.tilPassword.error = it
            }
        }

        collectWhenStarted {
            viewModel.channel.collectLatest { event ->
                when (event) {
                    is LoginViewModel.NavigationEvents.NavigateToMainScreen -> {
                        /*findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                event.loginResponse
                            )
                        )*/
                    }
                }
            }
        }
    }

}