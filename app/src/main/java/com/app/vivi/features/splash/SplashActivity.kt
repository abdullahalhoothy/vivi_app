package com.app.vivi.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.vivi.entrypoint.MainActivity
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.homescreen.home.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Splash Screen
        val splashScreen = installSplashScreen()

        // Keep the splash screen visible until the API response is ready
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        addObservers()
    }

    override fun onStart() {
        super.onStart()
        getConfigurations()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
//        finish()
    }

    private fun addObservers() {
        collectWhenStarted {
            splashViewModel.channel.collectLatest { event ->
                keepSplashOnScreen = false
                when(event){
                    is SplashViewModel.NavigationEvents.NavigateToMainScreen ->{
                        // Navigate to next screen based on API response
                        navigateToMainActivity()
                    } else -> {
                    keepSplashOnScreen = true
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
