package com.app.vivi.domain.interactors

import android.util.Patterns

class EmailValidationUseCase {


    fun isValid(email: String): Boolean {
        return if (email.isEmpty()) false
        else Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }
}