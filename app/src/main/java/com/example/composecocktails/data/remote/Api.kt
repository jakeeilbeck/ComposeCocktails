package com.example.composecocktails.data.remote

import com.example.composecocktails.data.models.Cocktail
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("random.php")
    suspend fun getRandomCocktail(): Cocktail

    @GET("search.php?")
    suspend fun searchCocktail(@Query("s") cocktailName: String): Cocktail

    companion object {
        const val BASE_URL = "https://thecocktaildb.com/api/json/v1/1/"
    }
}