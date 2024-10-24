package com.app.vivi.domain.interactors

class PasswordValidationUseCase {

    fun isValid(password: String): Boolean {
        return if (password.isEmpty()) {
            false
        } else {
            true
        }
    }

}