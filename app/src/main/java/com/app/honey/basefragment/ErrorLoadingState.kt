package com.app.honey.basefragment

import com.app.honey.domain.model.ErrorModel

sealed class ErrorLoadingState {
    object Loading : ErrorLoadingState()
    data class Error(val errorModel: ErrorModel) : ErrorLoadingState()
    data class Success(val errorModel: ErrorModel) : ErrorLoadingState()
    object Idle : ErrorLoadingState()
}
