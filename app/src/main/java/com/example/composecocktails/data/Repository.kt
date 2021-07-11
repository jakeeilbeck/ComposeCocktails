package com.example.composecocktails.data

import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.data.remote.Api

class Repository(private val api: Api) {

    suspend fun getRandomCocktail(): List<Cocktail.Drink?>?{
        return api.getRandomCocktail().drinks
    }

    suspend fun searchCocktail(cocktailName: String): List<Cocktail.Drink?>? {
        return api.searchCocktail(cocktailName).drinks
    }
}