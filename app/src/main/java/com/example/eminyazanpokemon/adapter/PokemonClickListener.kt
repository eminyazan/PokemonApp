package com.example.eminyazanpokemon.adapter

import com.example.eminyazanpokemon.model.PokemonModel

interface PokemonClickListener {
    fun pokemonClicked(pokemon: PokemonModel)
}