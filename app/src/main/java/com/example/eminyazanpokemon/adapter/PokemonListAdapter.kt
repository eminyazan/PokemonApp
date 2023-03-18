package com.example.eminyazanpokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.eminyazanpokemon.databinding.PokemonRowItemBinding
import com.example.eminyazanpokemon.model.PokemonModel
class PokemonListAdapter(
    private var clickListener: PokemonClickListener,
) :
    RecyclerView.Adapter<PokemonListAdapter.MViewHolder>() {

    override fun getItemCount(): Int = differ.currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder = MViewHolder.from(parent)

    override fun onBindViewHolder(holder: MViewHolder, position: Int) =
        holder.bind(clickListener, differ.currentList[position])


    private val differCallback = object : DiffUtil.ItemCallback<PokemonModel>() {
        override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

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