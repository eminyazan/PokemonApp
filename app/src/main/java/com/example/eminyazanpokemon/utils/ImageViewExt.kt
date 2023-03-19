package com.example.eminyazanpokemon.utils

 import android.net.Uri
import android.widget.ImageView
import coil.load

fun ImageView.loadImage(url: String) {
    this.load(Uri.parse(url)) {
        this.crossfade(true)
        this.crossfade(500)
    }
}

