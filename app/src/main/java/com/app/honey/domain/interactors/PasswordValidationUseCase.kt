package com.app.honey.domain.interactors

class PasswordValidationUseCase {

    fun isValid(password: String): Boolean {
        return if (password.isEmpty()) {
            false
        } else {
            true
        }
    }

}