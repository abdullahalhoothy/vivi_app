package com.app.vivi.features.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentLoginMainBinding
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.errorDialog
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.setDrawableWithSize
import com.app.vivi.extension.successDialog
import com.app.vivi.extension.toJson
import com.facebook.CallbackManager
import com.facebook.FacebookActivity
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginMainFragment : BaseFragmentVB<FragmentLoginMainBinding>(FragmentLoginMainBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookLoginLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleLoginApi()
        initFacebookCallbackManager()
//        performFacebookLogin()
        configureListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvTagline.text =
                getString(R.string.buy_the_right_txt, getString(R.string.app_name))

            googleButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_google,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))

            facebookButton.setDrawableWithSize(requireContext(),
                R.drawable.ic_facebook,
                70, 70,
                resources.getDimensionPixelSize(R.dimen.sdp_10))

            /*ivBackground.post {
                val startY = tvTitle.y // Start position from top of tvTitle
                val endY = 0f // End position at the top of the screen

                val animation = TranslateAnimation(0f, 0f, startY, endY)
                animation.duration = 2000 // Duration in milliseconds (e.g., 2 seconds)
                animation.fillAfter = true // Keeps the view at the final position after animation ends
                animation.repeatCount = Animation.INFINITE // Repeat indefinitely
                animation.repeatMode = Animation.RESTART

                ivBackground.startAnimation(animation)
            }*/

//            val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_animation)
//            ivBackground.startAnimation(slideUpAnimation)
//            val animationDrawable = ivBackground.drawable as AnimationDrawable
//            animationDrawable.start()

            emailButton.setOnClickListener {
                val action = LoginMainFragmentDirections.actionLoginMainFragmentToLoginFragment()
                findNavController().navigateWithSingleTop(action)
            }
            googleButton.setOnClickListener {
                googleSignInClient.signInIntent.let { signInIntent ->
                    googleSignInLauncher.launch(signInIntent)
                }
            }
            facebookButton.setOnClickListener {
                launchFacebookLoginActivity()
//                performFacebookLogin()
            }
        }

        addObservers()
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {
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

    //===============  Facebook login ==============

//    private lateinit var callbackManager: CallbackManager
//    private lateinit var facebookLoginLauncher: ActivityResultLauncher<Intent>

    // Initialize CallbackManager
    private fun initFacebookCallbackManager(){
        callbackManager = CallbackManager.Factory.create()
    }

    // Facebook login process
    private fun performFacebookLogin() {
        // Start the Facebook Login process when the custom button is clicked
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))

        // Register a callback for the login result
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // Handle successful login
                val accessToken = result.accessToken
                Log.d("FacebookLogin", "Access Token: ${accessToken.token}")
                navigateToHomeScreensGraph()
                // You can perform additional actions after successful login
            }

            override fun onCancel() {
                // Handle login cancellation
                Log.d("FacebookLogin", "Login Cancelled")
            }

            override fun onError(error: FacebookException) {
                // Handle error during login
                showErrorDialog(error.message.toString())
                Log.e("FacebookLogin", "Error: ${error.message}")
            }
        })
    }

    // Configure the ActivityResultLauncher
    private fun configureListener(){
        facebookLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // This is where we forward the result to the callback manager
            callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
        }
    }

    // Call this when Facebook login activity is launched
    private fun launchFacebookLoginActivity() {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        val loginIntent = Intent(requireContext(), FacebookActivity::class.java)

        // Start the Facebook login flow
        facebookLoginLauncher.launch(loginIntent)
//        val loginIntent = LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
//        facebookLoginLauncher.launch(loginIntent)
    }


    /* private fun initFacebookCallbackManager(){
         callbackManager = CallbackManager.Factory.create()
     }


     private fun performFacebookLogin() {
         // Start the Facebook Login process when the custom button is clicked
         LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
         // Register a callback for the login result
         LoginManager.getInstance().registerCallback(callbackManager, object :
             FacebookCallback<LoginResult> {
             override fun onSuccess(result: LoginResult) {
                 // Handle successful login
                 val accessToken = result?.accessToken
                 Log.d("FacebookLogin", "Access Token: ${accessToken?.token}")
                 navigateToHomeScreensGraph()
                 // You can perform additional actions after successful login
             }

             override fun onCancel() {
                 // Handle login cancellation
                 Log.d("FacebookLogin", "Login Cancelled")
             }

             override fun onError(error: FacebookException) {
                 // Handle error during login

                 showErrorDialog(error?.message.toString())
                 Log.e("FacebookLogin", "Error: ${error?.message}")
             }
         })
     }

     private fun configureListener(){
         facebookLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
             ActivityResultCallback { result ->
                 callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
             })
     }*/

    // =========== Facebook login End ===================

    //============ Navigation functions ===============
    private fun navigateToHomeScreensGraph(){
        findNavController().navigate(
            LoginMainFragmentDirections.actionLoginMainFragmentToHomeGraph(
            )
        )
    }

    private fun showErrorDialog(message: String){
        var dialog: androidx.appcompat.app.AlertDialog? = null
        dialog = errorDialog(ErrorModel(getString(R.string.error_title),
            getString(R.string.sign_in_failed_plz_try_again_txt), 400)){

        }

        dialog.show()
    }
}