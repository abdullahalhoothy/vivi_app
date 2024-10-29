package com.app.vivi.data.remote

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val title: String = "",
                     val message: String = "",
                     val code: Int = 0) : Resource<Nothing>()
//    data class Error(val error: String) : Resource<Nothing>()
}