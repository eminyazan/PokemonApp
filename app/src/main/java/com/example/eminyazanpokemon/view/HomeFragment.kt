package com.example.eminyazanpokemon.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.eminyazanpokemon.adapter.PokemonClickListener
import com.example.eminyazanpokemon.adapter.PokemonListAdapter
import com.example.eminyazanpokemon.base.BaseFragment
import com.example.eminyazanpokemon.databinding.FragmentHomeBinding
import com.example.eminyazanpokemon.model.PokemonModel
import com.example.eminyazanpokemon.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPokemons()
    }

    override fun onCreateFinished() {
    }

    override fun initializeListeners() {
    }

    override fun observeEvents() {
        with(viewModel) {
            apiResponse.observe(viewLifecycleOwner, Observer {
                it?.let {
                    println("api response observer ---> ${it.results?.size}")
                    it.results?.let { it1 -> setRecycler(it1) }
                }
            })
            loading.observe(viewLifecycleOwner, Observer {
                println("loading observer ---> $it")
                handleViews(it)
            })

            errorMessage.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }

    }

    private fun setRecycler(pokemons: List<PokemonModel>) {
        val mAdapter = PokemonListAdapter(object : PokemonClickListener {
            override fun pokemonClicked(pokemon: PokemonModel) {
                // TODO: move it to detail page
            }

        })

        binding.rvHome.adapter = mAdapter
        mAdapter.setPokemonList(pokemons)
    }


    private fun handleViews(isLoading: Boolean = false) {
        if (isLoading) {
            binding.pbHome.visibility = View.VISIBLE
            binding.rvHome.visibility = View.GONE
        } else {
            binding.pbHome.visibility = View.GONE
            binding.rvHome.visibility = View.VISIBLE
        }

    }

}