package com.example.eminyazanpokemon.view.home.repository

import com.example.eminyazanpokemon.base.BaseRepository
import com.example.eminyazanpokemon.network.ApiFactory
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiFactory: ApiFactory) : BaseRepository() {

    suspend fun getPokemons(offset: String, limit: String = "20") =
        safeApiRequest { apiFactory.getPokemons(offset, limit) }
}