package com.example.eminyazanpokemon.network

import com.example.eminyazanpokemon.model.ApiResponse
import com.example.eminyazanpokemon.model.PokemonDetailModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFactory {
    @GET(".")
    suspend fun getPokemons(
        @Query("offset") offset: String = 20.toString(),
        @Query("limit") limit: String = 20.toString(),
    ): ApiResponse

    @GET("/{id}")
    suspend fun getPokemon(@Path("id") id: String): PokemonDetailModel

}