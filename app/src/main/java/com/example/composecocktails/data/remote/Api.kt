package com.example.composecocktails.data.remote

import com.example.composecocktails.data.models.Cocktail
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("random.php")
    suspend fun searchRandomCocktail(): Cocktail

    @GET("search.php?")
    suspend fun searchCocktailByName(@Query("s") cocktailName: String): Cocktail

    @GET("filter.php?")
    suspend fun searchCocktailByIngredient(@Query("i") ingredient: String): Cocktail

    @GET("lookup.php?")
    suspend fun searchCocktailById(@Query("i") id: String): Cocktail

    companion object {
        const val BASE_URL = "https://thecocktaildb.com/api/json/v1/1/"
    }
}