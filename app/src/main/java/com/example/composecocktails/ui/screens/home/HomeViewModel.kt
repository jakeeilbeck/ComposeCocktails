package com.example.composecocktails.ui.screens.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecocktails.data.Repository
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.data.remote.Api
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = Repository(Api.create())
    val randomCocktailList = mutableStateListOf<Cocktail.Drink?>()
    var searchedCocktailList by mutableStateOf<List<Cocktail.Drink?>?>(null)
    var cocktailAdditionalInfo by mutableStateOf<Cocktail.Drink?>(null)
    var noResults = mutableStateOf(false)
    var searchTerm = mutableStateOf("")


    init {
        getRandomCocktails()
    }

    private fun getRandomCocktails() {
        viewModelScope.launch {
            if (randomCocktailList.isEmpty()) {
                for (i in 1..10) {
                    val randomCocktail = repository.getRandomCocktail()
                    randomCocktailList.add(randomCocktail?.get(0))
                }
            }
        }
    }

    fun searchCocktail(cocktailName: String) {
        viewModelScope.launch {
            val searchedCocktails = repository.searchCocktail(cocktailName)
            if (searchedCocktails == null) {
                searchTerm.value = cocktailName
                noResults.value = true
            } else {
                noResults.value = false
                searchedCocktailList = listOf()
                searchedCocktailList = searchedCocktails
            }
        }
    }
}