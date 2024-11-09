package com.app.vivi.features.notification

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginEmailBinding
import com.app.vivi.databinding.FragmentNotificationsBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotificationsFragment : BaseFragmentVB<FragmentNotificationsBinding>(FragmentNotificationsBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        handleBackPress()
        addObservers()
    }

    private fun initViews(){
        binding.apply {
            tvDescription.text = getString(R.string.no_notifications_description_txt, getString(R.string.app_name))

        }
    }


    private fun handleBackPress() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {

        collectWhenStarted {
            viewModel.channelLoginEmail.collectLatest { event ->
                when (event) {
                    is LoginViewModel.NavigationLoginEmailEvents.NavigateToLoginScreen -> {
                        /*findNavController().navigateWithSingleTop(
                            LoginEmailFragmentDirections.actionLoginEmailFragmentToLoginFragment()
                        )*/
                    }
                }
            }
        }
    }

}