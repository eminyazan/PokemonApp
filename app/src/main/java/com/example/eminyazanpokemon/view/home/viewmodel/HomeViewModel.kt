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

    //Data stuff
    val apiResponse: MutableLiveData<ApiResponse?> = MutableLiveData()
    private var newApiResponse: ApiResponse? = null

    //Loading Stuff
    val paginationLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val initialLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    private var offset = 0
    var pageNum = 0
    fun getPokemons() = viewModelScope.launch {

        if (newApiResponse == null) {
            initialLoading.value = true
        } else {
            paginationLoading.value = true
        }

        when (val request = homeRepository.getPokemons(offset = offset.toString())) {
            is NetworkResult.Success -> {
                //initial loading
                if (newApiResponse == null) {
                    // if its null that mean is its initial query

                    request.data?.let { apiRes ->
                        // first query
                        apiResponse.value = apiRes
                        newApiResponse = apiRes
                        println("view model api res length --->${apiResponse.value!!.results?.size}")
                        offset += 20
                        pageNum++
                        initialLoading.value = false
                    }


                } else {
                    //pagination query
                    request.data?.let { reqData ->
                        reqData.results?.let { newPokemons ->
                            apiResponse.value?.results?.addAll(newPokemons)
                        }
                        newApiResponse = reqData
                        println("view model api res length --->${apiResponse.value!!.results?.size}")
                        offset += 20
                        pageNum++
                        paginationLoading.value = false
                    }

                }
            }
            is NetworkResult.Error -> {
                errorMessage.value = request.message
                initialLoading.value = false
                paginationLoading.value = false
            }
        }
    }
}