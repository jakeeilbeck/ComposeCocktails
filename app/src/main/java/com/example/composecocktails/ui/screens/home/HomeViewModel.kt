package com.example.composecocktails.ui.screens.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecocktails.data.Repository
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val utils: Utils
) : ViewModel() {

    var randomCocktailList = mutableStateListOf<Cocktail.Drink?>()
    var searchedCocktailList by mutableStateOf<List<Cocktail.Drink?>?>(null)
    var cocktailAdditionalInfo by mutableStateOf<Cocktail.Drink?>(null)
    var searchTerm = mutableStateOf("")
    var searchError = mutableStateOf<ErrorType>(ErrorType.NoError)
    var generalError = mutableStateOf<ErrorType>(ErrorType.NoError)
    private var blankRandomCocktails = true
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getRandomCocktails()
    }

    fun getRandomCocktails() {
        if (utils.isOnlineCheck()) {
            viewModelScope.launch {
                try {

                    _isRefreshing.emit(true)

                    if (randomCocktailList.isEmpty() || isRefreshing.value) {
                        randomCocktailList.clear()
                        for (i in 1..10) {
                            val randomCocktail = repository.getRandomCocktail()
                            randomCocktailList.add(randomCocktail?.get(0))
                        }
                    }

                    blankRandomCocktails = false
                    _isRefreshing.emit(false)
                    generalError.value = ErrorType.NoError

                } catch (e: Exception) {

                    generalError.value = ErrorType.OtherError
                }
            }
        } else {
            //no internet connection
            generalError.value = ErrorType.NoConnection

            //add blank cocktails for placeholders to draw on top of
            val blankCocktail: Cocktail.Drink? = null
            for (i in 1..10) randomCocktailList.add(blankCocktail)
            blankRandomCocktails = true
        }
    }

    fun searchCocktail(cocktailName: String) {
        if (utils.isOnlineCheck()) {
            viewModelScope.launch {
                try {

                    val searchedCocktails = repository.searchCocktail(cocktailName)

                    if (searchedCocktails == null) {
                        searchTerm.value = cocktailName
                        searchError.value = ErrorType.NoResult
                    } else {
                        searchedCocktailList = listOf()
                        searchedCocktailList = searchedCocktails
                        searchError.value = ErrorType.NoError
                    }

                    //populate carousel random list for cases where there wasn't an internet connection on app start
                    if (blankRandomCocktails) getRandomCocktails()

                } catch (e: Exception) {

                    generalError.value = ErrorType.OtherError
                }
            }
        } else {
            //no internet connection
            generalError.value = ErrorType.NoConnection
        }
    }
}

sealed class ErrorType {
    object NoConnection : ErrorType()
    object NoResult : ErrorType()
    object OtherError : ErrorType()
    object NoError : ErrorType()
}