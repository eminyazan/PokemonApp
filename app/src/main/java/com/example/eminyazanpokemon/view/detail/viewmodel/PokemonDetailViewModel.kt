package com.example.eminyazanpokemon.view.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eminyazanpokemon.model.PokemonDetailModel
import com.example.eminyazanpokemon.utils.NetworkResult
import com.example.eminyazanpokemon.view.detail.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    var pokemon: MutableLiveData<PokemonDetailModel?> = MutableLiveData()

    //Loading Stuff
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    //error stuff
    val errorMessage: MutableLiveData<String?> = MutableLiveData()


    fun getPokemon(id: String) {
        loading.value = true

        viewModelScope.launch {

            when (val request = detailRepository.getPokemon(id)) {
                is NetworkResult.Success -> {
                    pokemon.value = request.data
                    loading.value = false
                }
                is NetworkResult.Error -> {
                    errorMessage.value = request.message
                    loading.value = false
                }
            }
        }
    }
}