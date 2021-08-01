package com.example.composecocktails.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composecocktails.data.models.Cocktail

@Database(entities = [Cocktail.Drink::class], version = 3, exportSchema = false)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract val favouritesDAO: FavouritesDAO
}