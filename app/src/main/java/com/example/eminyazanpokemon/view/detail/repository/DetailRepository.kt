package com.example.eminyazanpokemon.view.detail.repository

import com.example.eminyazanpokemon.base.BaseRepository
import com.example.eminyazanpokemon.network.ApiFactory
import javax.inject.Inject

class DetailRepository @Inject constructor(private val apiFactory: ApiFactory) : BaseRepository() {

    suspend fun getPokemon(id: String) =
        safeApiRequest {
            apiFactory.getPokemon(id)
        }
}