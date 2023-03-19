package com.example.eminyazanpokemon.view.detail

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.eminyazanpokemon.base.BaseFragment
import com.example.eminyazanpokemon.databinding.FragmentPokemonDetailBinding
import com.example.eminyazanpokemon.model.PokemonDetailModel
import com.example.eminyazanpokemon.service.FloatingWindowApp
import com.example.eminyazanpokemon.utils.ARGS_POKEMON_ID
import com.example.eminyazanpokemon.utils.WindowData
import com.example.eminyazanpokemon.view.detail.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment :
    BaseFragment<FragmentPokemonDetailBinding, PokemonDetailViewModel>(FragmentPokemonDetailBinding::inflate) {

    override val viewModel by viewModels<PokemonDetailViewModel>()

    private lateinit var pokemonId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonId = arguments?.getString(ARGS_POKEMON_ID).toString()
        viewModel.getPokemon(pokemonId)
    }

    override fun observeEvents() {

        observePokemon()

        observeLoading()

        observeError()

    }

    private fun showInWindow(pokemon: PokemonDetailModel) {
        //kill service if its running
        activity?.stopService(Intent(requireContext(), FloatingWindowApp::class.java))
        activity?.finish()
        // update our object data
        WindowData.pokemonForFloat = pokemon
        //start service
        activity?.startService(Intent(context, FloatingWindowApp::class.java))
        activity?.finish()

    }
    // ----------------------------- O B S E R V E R S ---------------------------------------------------- //

    private fun observeError() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            showError(it)
            hideProgressBar()
        }
    }

    private fun observePokemon() {
        viewModel.pokemon.observe(viewLifecycleOwner) { viewModelPokemon ->
            if (viewModelPokemon != null) {
                binding.pokemonDetail = viewModelPokemon
                binding.groupPokemonWidgets.isVisible = true
                binding.btnDetailOpenWindow.setOnClickListener {
                    showInWindow(viewModelPokemon)
                }
            }
        }
    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.pbDetail.isVisible = it
        }
    }
    // ----------------------------- O B S E R V E R S ---------------------------------------------------- //

    // ----------------------------- W I D G E T S ---------------------------------------------------- //

    private fun hideProgressBar() {
        binding.pbDetail.isVisible = false
    }

    private fun showError(errorMessage: String?) {
        binding.tvError.text = errorMessage
        binding.tvError.isVisible = true
    }

    // ----------------------------- W I D G E T S ---------------------------------------------------- //

}