package com.app.vivi.features.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginBinding
import com.app.vivi.databinding.FragmentLoginEmailBinding
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.successDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class LoginEmailFragment :
    BaseFragmentVB<FragmentLoginEmailBinding>(FragmentLoginEmailBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnContinue.setOnClickListener {
                viewModel.onLoginContinueClicked(
                    tieEmail.text.toString()
                )
            }

            tieEmail.doOnTextChanged { text, start, before, count ->
                viewModel.onEmailTextChanged(text.toString())
            }

            handleBackPress()

        }

        addObservers()
    }


    private fun handleBackPress() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {

        collectWhenStarted {
            viewModel.resetPasswordObserver.collectLatest { response ->

                response?.let {
                    var dialog: AlertDialog? = null
                    Log.d("ResetPasswordObserver", "Value: ${response}")

                    val data = ErrorModel(
                        getString(R.string.success_txt),
                        getString(R.string.a_password_reset_email_has_been_sent_to_your_registered_email_address_txt),
                        200
                    )
                    // Show the dialog
                    dialog = successDialog(data) {
                        // Handle button click
                        dialog?.dismiss() // Dismiss the dialog
                        findNavController().popBackStack() // Navigate back if needed
                    }

                    // Display the dialog
                    dialog.show()
                }

            }
        }
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
            viewModel.channelLoginEmail.collectLatest { event ->
                when (event) {
                    is LoginViewModel.NavigationLoginEmailEvents.NavigateToLoginScreen -> {
//                        getResetEmail(binding.tieEmail.text.toString())
                    }
                }
            }
        }

    }


}