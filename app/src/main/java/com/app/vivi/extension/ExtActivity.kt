package com.app.vivi.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import kotlinx.coroutines.launch

fun AppCompatActivity.collectWhenStarted(action: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action()
        }
    }
}

fun NavController.navigateWithSingleTop(action: NavDirections) {
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .build()
    this.navigate(action, navOptions)
}