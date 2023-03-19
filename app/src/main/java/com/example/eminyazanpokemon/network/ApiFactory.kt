package com.example.eminyazanpokemon.network

import com.example.eminyazanpokemon.model.ApiResponse
import com.example.eminyazanpokemon.model.PokemonDetailModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFactory {
    @GET(".")
    suspend fun getPokemons(
        @Query("offset") offset: String,
        @Query("limit") limit: String,
    ): ApiResponse

    @GET("{id}")
    suspend fun getPokemon(@Path(value = "id") id: String): PokemonDetailModel

}