package com.example.eminyazanpokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eminyazanpokemon.databinding.PokemonRowItemBinding
import com.example.eminyazanpokemon.model.PokemonModel


class PokemonListAdapter(private var clickListener: PokemonClickListener) :
    RecyclerView.Adapter<PokemonListAdapter.MViewHolder>() {

    private var pokemons = emptyList<PokemonModel>()
    override fun getItemCount(): Int = pokemons.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder = MViewHolder.from(parent)

    override fun onBindViewHolder(holder: MViewHolder, position: Int) =
        holder.bind(clickListener, pokemons[position])

    fun setPokemonList(newList: List<PokemonModel>) {
        pokemons = newList
        notifyDataSetChanged()
    }


    class MViewHolder(private val binding: PokemonRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: PokemonClickListener, pokemon: PokemonModel) {
            binding.clickListener = clickListener
            binding.pokemon = pokemon
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PokemonRowItemBinding.inflate(layoutInflater, parent, false)
                return MViewHolder(binding)
            }
        }


    }


}