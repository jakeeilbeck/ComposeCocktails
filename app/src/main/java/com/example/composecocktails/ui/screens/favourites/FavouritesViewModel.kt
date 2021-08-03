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
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {

    var favouriteCocktails = repository.getAllFavourites()
    var cocktailAdditionalInfo by mutableStateOf<Cocktail.Drink?>(null)

    fun deleteCocktail(cocktailId: String){
        viewModelScope.launch {
            repository.deleteId(cocktailId)
        }
    }
}