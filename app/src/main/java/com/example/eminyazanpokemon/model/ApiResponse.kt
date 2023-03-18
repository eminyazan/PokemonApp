package com.example.eminyazanpokemon.model


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("results")
    val results: ArrayList<PokemonModel>?,
    @SerializedName("count")
    val totalData: Int?
)