package com.example.composecocktails.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.composecocktails.data.models.Cocktail

@Dao
interface FavouritesDAO {

    @Insert
    suspend fun insert(cocktail: Cocktail.Drink)

    @Update
    suspend fun update(cocktail: Cocktail.Drink)

    @Delete
    suspend fun delete(cocktail: Cocktail.Drink)

    @Query("SELECT * FROM favourites_table WHERE idDrink = :cocktailId")
    suspend fun getById(cocktailId: String): Cocktail.Drink

    @Query("DELETE FROM favourites_table WHERE idDrink = :cocktailId")
    suspend fun deleteById(cocktailId: String)

    @Query("SELECT count(idDrink) FROM favourites_table  WHERE idDrink = :cocktailID")
    suspend fun checkIsFavourite(cocktailID: String?): Int

    @Query("SELECT * FROM favourites_table ORDER BY strDrink ASC")
    fun getAllFavourites(): LiveData<List<Cocktail.Drink>>
}