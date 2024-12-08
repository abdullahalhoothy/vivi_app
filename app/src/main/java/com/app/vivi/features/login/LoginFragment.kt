package com.app.vivi.features.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.setDrawableWithSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class LoginFragment : BaseFragmentVB<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvTitle.text = getString(R.string.welcome_back_log_in_to_your_account_txt, getString(R.string.app_name))
            btnLogin.setOnClickListener {
                viewModel.onLoginClicked(
                    tieEmail.text.toString(),
                    tiePassword.text.toString()
                )
            }
            tvResetPassword.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToLoginEmailFragment(
                    )
                )
            }

            tieEmail.doOnTextChanged { text, start, before, count ->
                viewModel.onEmailTextChanged(text.toString())
            }


            tiePassword.doOnTextChanged { text, start, before, count ->
                viewModel.onPasswordChanged(text.toString())
            }

            handleBackPress()

            googleButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_google,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))

            facebookButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_facebook,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))
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
                binding.tilPassword.error = it
            }
        }

        collectWhenStarted {
            viewModel.channel.collectLatest { event ->
                when (event) {
                    is LoginViewModel.NavigationEvents.NavigateToMainScreen -> {
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeGraph(
                            )
                        )
                    }
                }
            }
        }
    }

}