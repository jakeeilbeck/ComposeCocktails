package com.example.composecocktails.data.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class Cocktail(
    @Json(name = "drinks")
    val drinks: List<Drink?>?
) {
    @Entity(tableName = "favourites_table")
    @JsonClass(generateAdapter = true)
    data class Drink(
        @NonNull
        @PrimaryKey
        @Json(name = "idDrink")
        val idDrink: String,
        @ColumnInfo
        @Json(name = "strDrink")
        val strDrink: String? = "",
        @ColumnInfo
        @Json(name = "strDrinkAlternate")
        val strDrinkAlternate: String? = "",
        @ColumnInfo
        @Json(name = "strTags")
        val strTags: String? = "",
        @ColumnInfo
        @Json(name = "strVideo")
        val strVideo: String? = "",
        @ColumnInfo
        @Json(name = "strCategory")
        val strCategory: String? = "",
        @ColumnInfo
        @Json(name = "strIBA")
        val strIBA: String? = "",
        @ColumnInfo
        @Json(name = "strAlcoholic")
        val strAlcoholic: String? = "",
        @ColumnInfo
        @Json(name = "strGlass")
        val strGlass: String? = "",
        @ColumnInfo
        @Json(name = "strInstructions")
        val strInstructions: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsES")
        val strInstructionsES: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsDE")
        val strInstructionsDE: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsFR")
        val strInstructionsFR: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsIT")
        val strInstructionsIT: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsZH-HANS")
        val strInstructionsZHHANS: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsZH-HANT")
        val strInstructionsZHHANT: String? = "",
        @ColumnInfo
        @Json(name = "strDrinkThumb")
        val strDrinkThumb: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient1")
        val strIngredient1: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient2")
        val strIngredient2: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient3")
        val strIngredient3: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient4")
        val strIngredient4: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient5")
        val strIngredient5: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient6")
        val strIngredient6: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient7")
        val strIngredient7: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient8")
        val strIngredient8: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient9")
        val strIngredient9: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient10")
        val strIngredient10: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient11")
        val strIngredient11: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient12")
        val strIngredient12: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient13")
        val strIngredient13: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient14")
        val strIngredient14: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient15")
        val strIngredient15: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure1")
        val strMeasure1: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure2")
        val strMeasure2: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure3")
        val strMeasure3: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure4")
        val strMeasure4: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure5")
        val strMeasure5: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure6")
        val strMeasure6: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure7")
        val strMeasure7: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure8")
        val strMeasure8: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure9")
        val strMeasure9: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure10")
        val strMeasure10: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure11")
        val strMeasure11: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure12")
        val strMeasure12: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure13")
        val strMeasure13: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure14")
        val strMeasure14: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure15")
        val strMeasure15: String? = "",
        @ColumnInfo
        @Json(name = "strImageSource")
        val strImageSource: String? = "",
        @ColumnInfo
        @Json(name = "strImageAttribution")
        val strImageAttribution: String? = "",
        @ColumnInfo
        @Json(name = "strCreativeCommonsConfirmed")
        val strCreativeCommonsConfirmed: String? = "",
        @ColumnInfo
        @Json(name = "dateModified")
        val dateModified: String? = "",
        @ColumnInfo
        var isFavourite: Boolean = false
    )
}