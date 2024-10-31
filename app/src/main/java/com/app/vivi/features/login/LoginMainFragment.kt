package com.app.vivi.features.login

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginBinding
import com.app.vivi.databinding.FragmentLoginMainBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.setDrawableWithSize
import com.app.vivi.extension.showShortToast
import com.app.vivi.features.homescreen.home.fragments.HomeFragmentLatestDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class LoginMainFragment : BaseFragmentVB<FragmentLoginMainBinding>(FragmentLoginMainBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvTagline.text =
                getString(R.string.buy_the_right_txt, getString(R.string.app_name))

            googleButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_male_placeholder,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))

            facebookButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_male_placeholder,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))

            ivBackground.post {
                val startY = tvTitle.y // Start position from top of tvTitle
                val endY = 0f // End position at the top of the screen

                val animation = TranslateAnimation(0f, 0f, startY, endY)
                animation.duration = 2000 // Duration in milliseconds (e.g., 2 seconds)
                animation.fillAfter = true // Keeps the view at the final position after animation ends
                animation.repeatCount = Animation.INFINITE // Repeat indefinitely
                animation.repeatMode = Animation.RESTART

                ivBackground.startAnimation(animation)
            }

//            val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_animation)
//            ivBackground.startAnimation(slideUpAnimation)
//            val animationDrawable = ivBackground.drawable as AnimationDrawable
//            animationDrawable.start()

            emailButton.setOnClickListener {
                val action = LoginMainFragmentDirections.actionLoginMainFragmentToLoginFragment()
                findNavController().navigateWithSingleTop(action)
            }

            /* tieEmail.doOnTextChanged { text, start, before, count ->
                viewModel.onEmailTextChanged(text.toString())
            }

            tiePassword.doOnTextChanged { text, start, before, count ->
                viewModel.onPasswordChanged(text.toString())
            }*/
        }

        addObservers()
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {
        collectWhenStarted {
            viewModel.emailErrorFlow.collectLatest {
//                binding.tilEmail.error = it
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