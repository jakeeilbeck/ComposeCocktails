package com.example.composecocktails.data.remote

import com.example.composecocktails.data.models.Cocktail
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Api {
    @GET("random.php")
    suspend fun getRandomCocktail(): Cocktail

    @GET("search.php?")
    suspend fun searchCocktail(@Query("s") cocktailName: String): Cocktail

    companion object {
        private const val BASE_URL = "https://thecocktaildb.com/api/json/v1/1/"

        private val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun create(): Api {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()
                .create(Api::class.java)
        }
    }
}