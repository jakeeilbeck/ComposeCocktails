package com.example.composecocktails.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecocktails.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {

    var favouriteCocktails = repository.getAllFavourites()

    fun deleteCocktail(cocktailId: String){
        viewModelScope.launch {
            repository.deleteId(cocktailId)
        }
    }
}