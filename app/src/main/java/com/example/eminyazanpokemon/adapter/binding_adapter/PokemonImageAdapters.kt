package com.example.eminyazanpokemon.adapter.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.eminyazanpokemon.utils.loadImage

class PokemonImageAdapters {
    companion object {

        @JvmStatic
        @BindingAdapter("load_pokemon_image")
        fun loadPokemonImage(imageView: ImageView, id: String) {
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$id.png"
            imageView.loadImage(imageUrl)
        }
    }
}