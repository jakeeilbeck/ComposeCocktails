package com.example.composecocktails.ui.screens.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecocktails.data.Repository
import com.example.composecocktails.data.models.Cocktail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {

    var favouriteCocktails = repository.getAllFavourites()
    var cocktailAdditionalInfo by mutableStateOf<Cocktail.Drink?>(null)
    var showAdditionalInfo = mutableStateOf(false)

    fun deleteCocktail(cocktailId: String){
        viewModelScope.launch {
            repository.deleteId(cocktailId)
        }
    }

    fun updateAdditionalInfo(cocktail: Cocktail.Drink?){
        //on item click, close additional info if already open
        if (cocktailAdditionalInfo == cocktail){
            showAdditionalInfo.value = false
            //delay setting to null so user doesn't see text change
            Timer(false).schedule(100) {
                //set to null so if condition can be false, otherwise re-clicking item will
                //still be true, and the additional info wont show
                cocktailAdditionalInfo = null
            }
        }else{
            cocktailAdditionalInfo = cocktail
            showAdditionalInfo.value = true
        }
    }
}