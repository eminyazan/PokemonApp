package com.example.eminyazanpokemon.utils

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val networkError: Boolean = false
) {

    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(networkError: Boolean, message: String?) :
        NetworkResult<T>(message = message, networkError = networkError, data = null)

}