package com.example.composecocktails.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.composecocktails.data.models.Cocktail

@Dao
interface FavouritesDAO {

    @Insert
    suspend fun insert(cocktail: Cocktail.Drink)

    @Delete
    suspend fun delete(cocktail: Cocktail.Drink)

    @Query("DELETE FROM favourites_table WHERE idDrink = :cocktailId")
    suspend fun deleteId(cocktailId: String)

    @Query("SELECT count(idDrink) FROM favourites_table  WHERE idDrink = :cocktailID")
    suspend fun checkIsFavourite(cocktailID: String?): Int

    @Query("SELECT * FROM favourites_table ORDER BY strDrink ASC")
    fun getAllFavourites(): LiveData<List<Cocktail.Drink>>
}