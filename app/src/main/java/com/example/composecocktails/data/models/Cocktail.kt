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
        var idDrink: String,
        @ColumnInfo
        @Json(name = "strDrink")
        var strDrink: String? = "",
        @ColumnInfo
        @Json(name = "strDrinkAlternate")
        var strDrinkAlternate: String? = "",
        @ColumnInfo
        @Json(name = "strTags")
        var strTags: String? = "",
        @ColumnInfo
        @Json(name = "strVideo")
        var strVideo: String? = "",
        @ColumnInfo
        @Json(name = "strCategory")
        var strCategory: String? = "",
        @ColumnInfo
        @Json(name = "strIBA")
        var strIBA: String? = "",
        @ColumnInfo
        @Json(name = "strAlcoholic")
        var strAlcoholic: String? = "",
        @ColumnInfo
        @Json(name = "strGlass")
        var strGlass: String? = "",
        @ColumnInfo
        @Json(name = "strInstructions")
        var strInstructions: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsES")
        var strInstructionsES: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsDE")
        var strInstructionsDE: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsFR")
        var strInstructionsFR: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsIT")
        var strInstructionsIT: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsZH-HANS")
        var strInstructionsZHHANS: String? = "",
        @ColumnInfo
        @Json(name = "strInstructionsZH-HANT")
        var strInstructionsZHHANT: String? = "",
        @ColumnInfo
        @Json(name = "strDrinkThumb")
        var strDrinkThumb: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient1")
        var strIngredient1: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient2")
        var strIngredient2: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient3")
        var strIngredient3: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient4")
        var strIngredient4: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient5")
        var strIngredient5: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient6")
        var strIngredient6: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient7")
        var strIngredient7: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient8")
        var strIngredient8: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient9")
        var strIngredient9: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient10")
        var strIngredient10: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient11")
        var strIngredient11: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient12")
        var strIngredient12: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient13")
        var strIngredient13: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient14")
        var strIngredient14: String? = "",
        @ColumnInfo
        @Json(name = "strIngredient15")
        var strIngredient15: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure1")
        var strMeasure1: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure2")
        var strMeasure2: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure3")
        var strMeasure3: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure4")
        var strMeasure4: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure5")
        var strMeasure5: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure6")
        var strMeasure6: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure7")
        var strMeasure7: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure8")
        var strMeasure8: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure9")
        var strMeasure9: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure10")
        var strMeasure10: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure11")
        var strMeasure11: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure12")
        var strMeasure12: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure13")
        var strMeasure13: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure14")
        var strMeasure14: String? = "",
        @ColumnInfo
        @Json(name = "strMeasure15")
        var strMeasure15: String? = "",
        @ColumnInfo
        @Json(name = "strImageSource")
        var strImageSource: String? = "",
        @ColumnInfo
        @Json(name = "strImageAttribution")
        var strImageAttribution: String? = "",
        @ColumnInfo
        @Json(name = "strCreativeCommonsConfirmed")
        var strCreativeCommonsConfirmed: String? = "",
        @ColumnInfo
        @Json(name = "dateModified")
        var dateModified: String? = "",
        @ColumnInfo
        var isFavourite: Boolean = false,
        @ColumnInfo
        var isUserCreated: Boolean = false,
    )
}