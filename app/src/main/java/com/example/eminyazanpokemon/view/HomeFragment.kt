package com.example.eminyazanpokemon.view

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eminyazanpokemon.R
import com.example.eminyazanpokemon.adapter.PokemonClickListener
import com.example.eminyazanpokemon.adapter.PokemonListAdapter
import com.example.eminyazanpokemon.base.BaseFragment
import com.example.eminyazanpokemon.databinding.FragmentHomeBinding
import com.example.eminyazanpokemon.manager.AppConnectivityManager
import com.example.eminyazanpokemon.model.PokemonModel
import com.example.eminyazanpokemon.service.FloatingWindowApp
import com.example.eminyazanpokemon.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel by viewModels<HomeViewModel>()
    private var isLoadingPagination = false
    private var isLastPage = false
    private var isScrolling = false
    private var pageSize = 20

    private var firstRequestSend = false


    private lateinit var appConnectivityManager: AppConnectivityManager
    private lateinit var dialog: AlertDialog

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkInternetAndPermission()
        if (firstRequestSend) {
            viewModel.getPokemons()
        }
    }

    override fun observeEvents() {

        observeList(viewModel)

        observeInitialLoading(viewModel)

        observePaginationLoading(viewModel)

        observeErrorState(viewModel)


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkInternetAndPermission() {
        //connection manager
        appConnectivityManager = AppConnectivityManager(requireContext())


        appConnectivityManager.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let { internet ->
                if (internet) {

                    // kill service
                    if (isServiceRunning()) {
                        activity?.stopService(Intent(requireContext(), FloatingWindowApp::class.java))
                    }


                    if (checkPermission()) {

                        //hide permission button we have permission
                        hidePermissionButton()

                        //we have internet
                        hideNoInternetWidgets()

                        //prevent request when go back from detail page
                        if (firstRequestSend) {
                            showRecyclerView()
                        } else {
                            viewModel.getPokemons()
                            firstRequestSend = true
                        }


                    } else {
                        //we have internet we do not have permission
                        hideNoInternetWidgets()

                        //Show button we do not have permission
                        showPermissionButton()

                        hideProgressBar()

                    }


                } else {
                    // No internet
                    showNoInternetWidgets()

                    //hide other widgets
                    hidePermissionButton()


                }

            }
        }

    }




    // ------------------------------------- W I D G E T S --------------------------------------//
    private fun hideProgressBar() {
        binding.pbHome.isVisible = false
    }

    private fun setRecycler(pokemons: ArrayList<PokemonModel>) {
        val mAdapter = PokemonListAdapter(object : PokemonClickListener {
            override fun pokemonClicked(pokemon: PokemonModel) {
                // https://pokeapi.co/api/v2/pokemon/1/  -> takes after pokemon/ -> 1/, 102/ ...
                val pattern = Regex("(?<=pokemon/).*")
                val regexRes = pattern.find(pokemon.url!!)
                val regex = regexRes!!.value // -> 125/  1/  10/
                val id = regex.dropLast(1) // -> 125   1   10
                val action = HomeFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(id)
                findNavController().navigate(action)
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

    private fun hidePermissionButton() {
        binding.btnOverlayPermissionButton.isVisible = false
    }

    private fun showRecyclerView() {
        binding.rvHome.isVisible = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionButton() {
        binding.btnOverlayPermissionButton.isVisible = true
        binding.btnOverlayPermissionButton.setOnClickListener {
            requestPermission()
        }
    }

    private fun showNoInternetWidgets() {
        binding.btnNoInternetRetry.isVisible = true
        binding.tvNoInternet.isVisible = true
        binding.btnNoInternetRetry.setOnClickListener {
            viewModel.getPokemons()
        }
    }

    private fun hideNoInternetWidgets() {
        binding.btnNoInternetRetry.isVisible = false
        binding.tvNoInternet.isVisible = false
    }

    // ------------------------------------- W I D G E T S --------------------------------------//

    // ------------------------------------- O B S E R V E R S ----------------------------------//
    private fun observePaginationLoading(viewModel: HomeViewModel) {
        viewModel.paginationLoading.observe(viewLifecycleOwner) {
            handlePaginationViews(it)
        }
    }

    private fun observeInitialLoading(viewModel: HomeViewModel) {
        viewModel.initialLoading.observe(viewLifecycleOwner) {
            handleInitialViews(it)
        }
    }

    private fun observeList(viewModel: HomeViewModel) {
        viewModel.apiResponse.observe(viewLifecycleOwner) {
            it?.let { apiRes ->
                apiRes.results?.let { pokemons -> setRecycler(pokemons) }

                // Total page ex: 1281 % 20 = 1  -> 1281 - 1 = 1280  -> 1280 / 20 = 64 -> total page
                val mod = (apiRes.totalData!! % pageSize)
                val pageSize = (apiRes.totalData - mod) / pageSize
                isLastPage = viewModel.pageNum == pageSize
            }
        }
    }

    private fun observeErrorState(viewModel: HomeViewModel) {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            binding.tvError.isVisible = true
            binding.btnRetry.isVisible = true
            binding.tvError.text = it

            binding.btnRetry.setOnClickListener {
                viewModel.getPokemons()
                viewModel.errorMessage.value = null
                binding.tvError.isVisible = false
                binding.btnRetry.isVisible = false
            }
        }
    }

    // ------------------------------------- O B S E R V E R S --------------------------------------//

    // ------------------------------------- P E R M I S S I O N ------------------------------------//

    private fun isServiceRunning(): Boolean {
        // is floating window is working
        activity?.let {
            val manager = it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (FloatingWindowApp::class.java.name == service.service.packageName) {
                    return true
                }
            }
        }

        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        val context = requireContext()
        // show dialog for permission
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setTitle(resources.getString(R.string.permission_error_title))
        builder.setMessage(resources.getString(R.string.permission_error_text))
        builder.setPositiveButton(resources.getString(R.string.common_ok)) { _, _ ->

            // open settings with package name
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            startActivityForResult(intent, AppCompatActivity.RESULT_OK)
        }
        dialog = builder.create()
        dialog.show()

    }


    private fun checkPermission(): Boolean {
        // we have permission after M otherwise it is false automatically
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(requireContext())
        } else {
            return false
        }
    }

    // ------------------------------------- P E R M I S S I O N--------------------------------------//


}