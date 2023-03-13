package com.example.eminyazanpokemon.view.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eminyazanpokemon.model.ApiResponse
import com.example.eminyazanpokemon.utils.NetworkResult
import com.example.eminyazanpokemon.view.home.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    val apiResponse: MutableLiveData<ApiResponse?> = MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    //initial offset from api
    private var _itemCount = 0

    fun getPokemons() = viewModelScope.launch {
        loading.value = true
        when (val request = homeRepository.getPokemons(offset = _itemCount.toString())) {
            is NetworkResult.Success -> {
                apiResponse.value = request.data
                loading.value = false
                //increase item for pagination
                _itemCount += 20
            }
            is NetworkResult.Error -> {
                errorMessage.value = request.message
                loading.value = false
            }
        }
    }


}