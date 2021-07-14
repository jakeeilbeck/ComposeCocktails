package com.example.composecocktails.ui.screens.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecocktails.data.Repository
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val utils: Utils
) : ViewModel() {

    val randomCocktailList = mutableStateListOf<Cocktail.Drink?>()
    var searchedCocktailList by mutableStateOf<List<Cocktail.Drink?>?>(null)
    var cocktailAdditionalInfo by mutableStateOf<Cocktail.Drink?>(null)
    var searchTerm = mutableStateOf("")
    var errorType = mutableStateOf<ErrorTypes>(ErrorTypes.NoError)

    init {
        getRandomCocktails()
    }

    private fun getRandomCocktails() {
        if (utils.isOnlineCheck()) {
            viewModelScope.launch {
                if (randomCocktailList.isEmpty()) {
                    for (i in 1..10) {
                        val randomCocktail = repository.getRandomCocktail()
                        randomCocktailList.add(randomCocktail?.get(0))
                    }
                }
                errorType.value = ErrorTypes.NoError
            }
        } else {
            errorType.value = ErrorTypes.NoConnection
        }
    }

    fun searchCocktail(cocktailName: String) {
        if (utils.isOnlineCheck()) {
            viewModelScope.launch {
                val searchedCocktails = repository.searchCocktail(cocktailName)
                if (searchedCocktails == null) {
                    searchTerm.value = cocktailName
                    errorType.value = ErrorTypes.NoResult
                } else {
                    searchedCocktailList = listOf()
                    searchedCocktailList = searchedCocktails
                    errorType.value = ErrorTypes.NoError
                }
                //populate random list for cases where there wasn't an internet connect on app start
                if (randomCocktailList.isEmpty()) getRandomCocktails()
            }
        } else {
            errorType.value = ErrorTypes.NoConnection
        }
    }
}

sealed class ErrorTypes {
    object NoConnection : ErrorTypes()
    object NoResult : ErrorTypes()
    object NoError : ErrorTypes()
}