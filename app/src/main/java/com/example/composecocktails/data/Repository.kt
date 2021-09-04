package com.example.composecocktails.data

import androidx.lifecycle.LiveData
import com.example.composecocktails.data.local.FavouritesDAO
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.data.remote.Api
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api: Api, private val favouritesDao: FavouritesDAO) {

    suspend fun getRandomCocktail(): List<Cocktail.Drink?>?{
        return api.getRandomCocktail().drinks
    }

    suspend fun searchCocktailByName(cocktailName: String): List<Cocktail.Drink?>? {
        return api.searchCocktailByName(cocktailName).drinks
    }

    suspend fun searchCocktailByIngredient(ingredient: String): List<Cocktail.Drink?>? {
        return api.searchCocktailByIngredient(ingredient).drinks
    }

    suspend fun insertCocktail(cocktail: Cocktail.Drink){
        favouritesDao.insert(cocktail)
    }

    suspend fun deleteCocktail(cocktail: Cocktail.Drink){
        favouritesDao.delete(cocktail)
    }

    suspend fun deleteId(cocktailId: String){
        favouritesDao.deleteId(cocktailId)
    }

    suspend fun checkFavourite(cocktailId: String): Int{
        return favouritesDao.checkIsFavourite(cocktailId)
    }

    fun getAllFavourites(): LiveData<List<Cocktail.Drink>> {
        return favouritesDao.getAllFavourites()
    }
}