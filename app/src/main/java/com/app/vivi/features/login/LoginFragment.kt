package com.app.vivi.features.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginBinding
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.errorDialog
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.setDrawableWithSize
import com.app.vivi.extension.toJson
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class LoginFragment : BaseFragmentVB<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleLoginApi()
    }

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
            tvSignup.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment(
                    )
                )
            }

            tvResetPassword.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToLoginEmailFragment(
                    )
                )
            }

            googleButton.setOnClickListener {
                googleSignInClient.signInIntent.let { signInIntent ->
                    googleSignInLauncher.launch(signInIntent)
                }
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



    //=============== Google Login =================
    private fun configureGoogleLoginApi(){

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

//        val auth = FirebaseAuth.getInstance()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = FirebaseAuth.getInstance()
    }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            handleSignInResult(data)
        } else {
            Log.e("GoogleSignIn", "Sign-in failed or canceled")
        }
    }

    private fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google sign-in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    task.result.user?.displayName
                    val user = auth.currentUser
                    user.toJson()
                    Log.d("GoogleSignIn", "Sign-in successful: ${user?.displayName} ${user.toJson()}")
                    viewModel.setLoginStatus(true)
                    navigateToHomeScreensGraph()
                } else {
                    showErrorDialog(getString(R.string.sign_in_failed_plz_try_again_txt))
                    Log.w("GoogleSignIn", "Sign-in failed", task.exception)
                }
            }
    }
    //===============  Google login End==============

    private fun navigateToHomeScreensGraph(){
        findNavController().navigateWithSingleTop(
            LoginFragmentDirections.actionLoginFragmentToHomeGraph(
            )
        )
    }

    private fun showErrorDialog(message: String){
        var dialog: androidx.appcompat.app.AlertDialog? = null
        dialog = errorDialog(
            ErrorModel(getString(R.string.error_title),
            getString(R.string.sign_in_failed_plz_try_again_txt), 400)
        ){

        }

        dialog.show()
    }

}