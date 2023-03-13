package com.example.eminyazanpokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.eminyazanpokemon.R
import com.example.eminyazanpokemon.databinding.PokemonRowItemBinding
import com.example.eminyazanpokemon.model.PokemonModel


class PokemonListAdapter(
    private val pokemons: ArrayList<PokemonModel>,
) :
    RecyclerView.Adapter<PokemonListAdapter.LaunchViewHolder>() {

    private lateinit var binding: PokemonRowItemBinding

    inner class LaunchViewHolder(var customView: PokemonRowItemBinding) :
        RecyclerView.ViewHolder(customView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.pokemon_row_item, parent, false)
        return LaunchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.customView.pokemon = pokemons[position]
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

}