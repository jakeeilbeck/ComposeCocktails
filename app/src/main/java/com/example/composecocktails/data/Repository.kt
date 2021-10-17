package com.example.composecocktails.data

import androidx.lifecycle.LiveData
import com.example.composecocktails.data.local.CocktailDAO
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.data.remote.Api
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api: Api, private val cocktailDao: CocktailDAO) {

    suspend fun searchRandomCocktail(): List<Cocktail.Drink?>?{
        return api.searchRandomCocktail().drinks
    }

    suspend fun searchCocktailByName(cocktailName: String): List<Cocktail.Drink?>? {
        return api.searchCocktailByName(cocktailName).drinks
    }

    suspend fun searchCocktailByIngredient(ingredient: String): List<Cocktail.Drink?>? {
        return api.searchCocktailByIngredient(ingredient).drinks
    }

    suspend fun searchCocktailById(Id: String): List<Cocktail.Drink?>? {
        return api.searchCocktailById(Id).drinks
    }

    suspend fun insertCocktail(cocktail: Cocktail.Drink){
        cocktailDao.insert(cocktail)
    }

    suspend fun updateCocktail(cocktail: Cocktail.Drink){
        cocktailDao.update(cocktail)
    }

    suspend fun getCocktailById(cocktailId: String): Cocktail.Drink{
        return cocktailDao.getById(cocktailId)
    }

    suspend fun deleteCocktail(cocktail: Cocktail.Drink){
        cocktailDao.delete(cocktail)
    }

    suspend fun deleteById(cocktailId: String){
        cocktailDao.deleteById(cocktailId)
    }

    suspend fun checkFavourite(cocktailId: String): Int{
        return cocktailDao.checkIsFavourite(cocktailId)
    }

    fun getAllFavourites(): LiveData<List<Cocktail.Drink>> {
        return cocktailDao.getAllFavourites()
    }

    fun getAllUserCreated(): LiveData<List<Cocktail.Drink>> {
        return cocktailDao.getAllUserCreated()
    }
}