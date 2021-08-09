package com.example.composecocktails.ui.screens

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
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository,
    private val utils: Utils
) : ViewModel() {

    val randomCocktailList = mutableStateListOf<Cocktail.Drink?>()
    val searchedCocktailList = mutableStateListOf<Cocktail.Drink?>()
    var cocktailAdditionalInfoHome by mutableStateOf<Cocktail.Drink?>(null)
    val showAdditionalInfoHome = mutableStateOf(false)
    var searchTerm = mutableStateOf("")
    var searchError = mutableStateOf<ErrorType>(ErrorType.NoError)
    var generalError = mutableStateOf<ErrorType>(ErrorType.NoError)
    private var blankRandomCocktails = true
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    var cocktailAdditionalInfoFavourites by mutableStateOf<Cocktail.Drink?>(null)
    val showAdditionalInfoFavourites = mutableStateOf(false)
    var favouriteCocktails = repository.getAllFavourites()

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
                        generalError.value = ErrorType.NoError
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

            if (cocktailIndex >= 0){
                searchedCocktailList[cocktailIndex] = replacement
            }

        }else{

            addToFavourites(cocktail)

            replacement.isFavourite = true
            searchedCocktailList[cocktailIndex] = replacement
        }
    }

    fun updateAdditionalInfo(cocktail: Cocktail.Drink?, screen: String){
        when(screen){
            "Home" ->{
                //on item click, close additional info if already open
                if (cocktailAdditionalInfoHome == cocktail){
                    showAdditionalInfoHome.value = false
                    //delay setting to null so user doesn't see text change
                    Timer(false).schedule(100) {
                        //set to null so if condition can be false, otherwise re-clicking item will
                        //still be true, and the additional info wont show
                        cocktailAdditionalInfoHome = null
                    }
                }else{
                    cocktailAdditionalInfoHome = cocktail
                    showAdditionalInfoHome.value = true
                }
            }
            "Favourites" ->{
                if (cocktailAdditionalInfoFavourites == cocktail){
                    showAdditionalInfoFavourites.value = false
                    Timer(false).schedule(100) {
                        cocktailAdditionalInfoFavourites = null
                    }
                }else{
                    cocktailAdditionalInfoFavourites = cocktail
                    showAdditionalInfoFavourites.value = true
                }
            }
        }
    }

    private fun checkIsFavourite(cocktailId: String) =
        runBlocking {
            repository.checkFavourite(cocktailId) == 1
        }

    private fun addToFavourites(cocktail: Cocktail.Drink){
        viewModelScope.launch {
            repository.insertCocktail(
                cocktail.apply { isFavourite = true }
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