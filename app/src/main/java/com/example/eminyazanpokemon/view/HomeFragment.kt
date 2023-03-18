package com.example.eminyazanpokemon.view

import android.os.Bundle
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eminyazanpokemon.adapter.PokemonClickListener
import com.example.eminyazanpokemon.adapter.PokemonListAdapter
import com.example.eminyazanpokemon.base.BaseFragment
import com.example.eminyazanpokemon.databinding.FragmentHomeBinding
import com.example.eminyazanpokemon.model.PokemonModel
import com.example.eminyazanpokemon.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel by viewModels<HomeViewModel>()
    private var isLoadingPagination = false
    private var isLastPage = false
    private var isScrolling = false
    private var pageSize = 20

    private val pokemonScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isLoadingPagination && !isLastPage
            val isAtLastItem = firstItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= pageSize
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getPokemons()
                isScrolling = false
            }


        }
    }

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
            apiResponse.observe(viewLifecycleOwner) {
                it?.let { apiRes ->
                    apiRes.results?.let { pokemons -> setRecycler(pokemons) }

                    // Total page ex: 1281 % 20 = 1  -> 1281 - 1 = 1280  -> 1280 / 20 = 64 -> total page
                    val mod = (apiRes.totalData!! % pageSize)
                    val pageSize = (apiRes.totalData - mod) / pageSize
                    isLastPage = pageNum == pageSize
                }
            }
            initialLoading.observe(viewLifecycleOwner) {
                println("loading observer inital---> $it")
                handleInitialViews(it)
            }
            paginationLoading.observe(viewLifecycleOwner) {
                println("loading observer pagination ---> $it")
                handlePaginationViews(it)
            }

            errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                binding.tvError.isVisible = true
                binding.btnRetry.isVisible = true
                binding.tvError.text = it

                binding.btnRetry.setOnClickListener {
                    viewModel.getPokemons()
                    errorMessage.value = null
                    binding.tvError.isVisible = false
                    binding.btnRetry.isVisible = false
                }
            }
        }

    }

    private fun setRecycler(pokemons: ArrayList<PokemonModel>) {
        val mAdapter = PokemonListAdapter(object : PokemonClickListener {
            override fun pokemonClicked(pokemon: PokemonModel) {
                // TODO: move it to detail page
            }

        })


        binding.rvHome.addOnScrollListener(this.pokemonScrollListener)
        binding.rvHome.adapter = mAdapter
        mAdapter.differ.submitList(pokemons)

    }


    private fun handleInitialViews(isLoading: Boolean = false) {
        binding.pbHome.isVisible = isLoading

        // close gap of pbBottom
        binding.rvHome.setPadding(0, 0, 0, 0)
        binding.rvHome.isVisible = !isLoading
        //always false
        binding.pbBottom.isVisible = false
    }

    private fun handlePaginationViews(isLoading: Boolean) {

        if (isLoading) {
            binding.pbBottom.isVisible = true
            //create gap bottom of the rv
            binding.rvHome.setPadding(0, 0, 0, 150)
        } else {
            binding.pbBottom.isVisible = false
            binding.rvHome.setPadding(0, 0, 0, 0)
        }


        if (isLastPage) {
            // if its last page remove gap
            binding.rvHome.setPadding(0, 0, 0, 0)
        }
    }

}