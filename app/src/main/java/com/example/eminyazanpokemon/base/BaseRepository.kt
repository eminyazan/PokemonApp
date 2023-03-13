package com.example.eminyazanpokemon.base

import com.example.eminyazanpokemon.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiRequest(
        apiRequest: suspend () -> T
    ): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(apiRequest.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        NetworkResult.Error(false, message = "HTTP Exception")
                    }
                    else -> {
                        NetworkResult.Error(true, message = "Network error")
                    }
                }
            }
        }
    }
}