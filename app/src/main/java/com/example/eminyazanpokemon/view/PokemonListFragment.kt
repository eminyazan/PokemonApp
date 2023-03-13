package com.example.eminyazanpokemon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eminyazanpokemon.adapter.PokemonListAdapter
import com.example.eminyazanpokemon.databinding.FragmentPokemonListBinding
import com.example.eminyazanpokemon.model.PokemonModel
import com.example.eminyazanpokemon.viewmodel.PokemonListViewModel

class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding

    private lateinit var pokemonListAdapter: PokemonListAdapter

    private lateinit var viewModel: PokemonListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemons = arrayListOf(
            PokemonModel("bbbbbbb", 54, 87),
            PokemonModel("baban", 54, 87),
            PokemonModel("eben", 54, 87),
            PokemonModel("aaan", 54, 87),
            PokemonModel("anan", 54, 87),
            PokemonModel("andan", 54, 87),
        )
        pokemonListAdapter = PokemonListAdapter(pokemons)
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.rvPokemons.adapter = pokemonListAdapter
        binding.rvPokemons.layoutManager = LinearLayoutManager(context)
    }

}