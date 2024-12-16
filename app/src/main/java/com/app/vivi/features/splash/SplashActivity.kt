package com.app.vivi.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.app.vivi.entrypoint.MainActivity
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.homescreen.home.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Splash Screena
        val splashScreen = installSplashScreen()

        // Keep the splash screen visible until the API response is ready
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, 5000) // Delay of 3 seconds
        addObservers()
    }

    override fun onStart() {
        super.onStart()
//        getConfigurations()
    }

    override fun onResume() {
        super.onResume()
//        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        Log.d("navigateToMainActivity", "Launching MainActivity")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            startActivity(intent)
            finish()
            Log.d("navigateToMainActivity", "MainActivity launched successfully")
        } catch (e: Exception) {
            Log.e("navigateToMainActivity", "Error launching MainActivity: ${e.message}")
        }
    }

    private fun addObservers() {
        collectWhenStarted {
            splashViewModel.channel.collectLatest { event ->
                keepSplashOnScreen = false

                Log.e("splashViewModel", "Value: ${event}")

                when(event){
                    is SplashViewModel.NavigationEvents.NavigateToMainScreen ->{
                        // Navigate to next screen based on API response
                        navigateToMainActivity()
                        Log.e("splashViewModel", "Value: ${event.response}")
                        /*Handler(Looper.getMainLooper()).postDelayed({
                            navigateToMainActivity()
                        }, 2000) */// Delay of 3 seconds
                    }
                }
            }
        }
    }

    private fun getConfigurations() {
        // Trigger API call
        splashViewModel.getConfigurations(true)
    }

    private fun showErrorDialog(message: String?) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message ?: "Something went wrong")
            .setPositiveButton("Retry") { _, _ -> splashViewModel.getConfigurations() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .show()
    }
}
