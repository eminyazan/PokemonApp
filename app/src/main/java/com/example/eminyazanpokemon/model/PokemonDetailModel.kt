package com.example.eminyazanpokemon.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("weight")
    val weight: Int?,
)

