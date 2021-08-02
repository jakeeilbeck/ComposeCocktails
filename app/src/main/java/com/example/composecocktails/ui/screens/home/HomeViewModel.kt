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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val utils: Utils
) : ViewModel() {

    val randomCocktailList = mutableStateListOf<Cocktail.Drink?>()
    val searchedCocktailList = mutableStateListOf<Cocktail.Drink?>()
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
                        searchedCocktailList.clear()

                        //Check if items are currently in favourites to show as such in the ui
                        searchedCocktails.map {
                            it?.isFavourite = checkIsFavourite(it?.idDrink.toString())
                        }

                        searchedCocktailList.addAll(searchedCocktails)

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

    fun updateFavourite(cocktail: Cocktail.Drink){
        /*
        https://stackoverflow.com/questions/66448722
        Replace clicked cocktail item with isFavourite reassigned instead of just reassigning
        isFavourite to existing item, as object reference needs to change for recomposition to
        happen to list item
        */

        val cocktailIndex = searchedCocktailList.indexOf(cocktail)
        val replacement = cocktail.copy()

        if (checkIsFavourite(cocktail.idDrink)){

            deleteFromFavourites(cocktail)

            replacement.isFavourite = false
            searchedCocktailList[cocktailIndex] = replacement
        }else{

            addToFavourites(cocktail)

            replacement.isFavourite = true
            searchedCocktailList[cocktailIndex] = replacement
        }
    }

    private fun checkIsFavourite(cocktailId: String) =
        runBlocking {
            repository.checkFavourite(cocktailId) == 1
        }

    private fun addToFavourites(cocktail: Cocktail.Drink){
        viewModelScope.launch {
            repository.insertCocktail(
                cocktail
            )
        }
    }

    private fun deleteFromFavourites(cocktail: Cocktail.Drink){
        viewModelScope.launch {
            repository.deleteCocktail(
                cocktail
            )
        }
    }
}

sealed class ErrorType(var errorMessage: String) {
    object NoConnection : ErrorType("No internet connection")
    object NoResult : ErrorType("No results for: ")
    object OtherError : ErrorType("Error getting results")
    object NoError : ErrorType("")
}