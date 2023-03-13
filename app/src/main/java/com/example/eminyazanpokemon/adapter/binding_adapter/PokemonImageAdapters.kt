package com.example.eminyazanpokemon.adapter.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.eminyazanpokemon.utils.loadImage

class PokemonImageAdapters {
    companion object {

        @BindingAdapter("load_pokemon_image")
        @JvmStatic
        fun loadPokemonImage(imageView: ImageView, id: Int?) {
            id?.let {
                val imageUrl =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$id.png"
                imageView.loadImage(imageUrl)
            }
        }
    }
}