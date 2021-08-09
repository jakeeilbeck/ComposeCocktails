package com.example.composecocktails.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.ui.theme.gradientErrorBackground

@SuppressLint("UnusedTransitionTargetStateParameter")
@ExperimentalMaterialApi
@Composable
fun CocktailListItem(
    modifier: Modifier = Modifier,
    cocktail: Cocktail.Drink,
    showInfo: (Cocktail.Drink?) -> Unit,
    updateFavourite: (Cocktail.Drink) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { showInfo(cocktail) },
        elevation = 2.dp
    ) {
        Row {
            ListItem(
                icon = {
                    Image(
                        painter = rememberImagePainter(
                            data = cocktail.strDrinkThumb,
                            builder = {crossfade(true)}
                        ),
                        modifier = modifier
                            .size(64.dp)
                            .clip(shape = MaterialTheme.shapes.small),
                        contentDescription = null
                    )
                },
                text = { Text(text = cocktail.strDrink.toString()) },
                secondaryText = { Text(text = cocktail.strCategory.toString()) },
                trailing =
                {
                    IconToggleButton(
                        checked = cocktail.isFavourite,
                        onCheckedChange = {
                            updateFavourite(cocktail)
                        }
                    ) {
                        //favourite click animation
                        val transition = updateTransition(
                            cocktail.isFavourite,
                            label = "isFavourite indicator"
                        )

                        //favourites icon animations
                        val animateTint by transition.animateColor(
                            label = "Tint"
                        ) {
                            if (cocktail.isFavourite) Color.Red else Color.LightGray
                        }

                        val animateSize by transition.animateDp(
                            transitionSpec = {
                                if (false isTransitioningTo true) {
                                    keyframes {
                                        durationMillis = 250
                                        35.dp at 0
                                        40.dp at 15
                                        52.dp at 75
                                        42.dp at 250
                                    }
                                } else {
                                    keyframes {
                                        durationMillis = 250
                                        52.dp at 0
                                        42.dp at 15
                                        35.dp at 75
                                        42.dp at 250
                                    }
                                }
                            },
                            label = "Size"
                        ) { 42.dp }

                        //update favourites icon with animations
                        Icon(
                            imageVector = if (cocktail.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = animateTint,
                            modifier = modifier
                                .size(animateSize),
                        )
                    }
                }
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ErrorBanner(
    errorText: String,
    modifier: Modifier = Modifier,
    searchTerm: String = "",
    visibility: Boolean,
    gradient: Brush = gradientErrorBackground()
) {
    val screenWidth = LocalContext
        .current
        .resources
        .displayMetrics
        .run { widthPixels }.toInt()

    AnimatedVisibility(
        visible = visibility,
        enter = expandHorizontally()
                + slideInHorizontally(),
        exit = slideOutHorizontally(
            targetOffsetX = { screenWidth })
                + shrinkHorizontally()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(0.dp, 32.dp, 0.dp, 32.dp)
                .background(brush = gradient)
                .fillMaxWidth()
        ) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier
                    .padding(
                        start = 0.dp,
                        top = 8.dp,
                        end = 0.dp,
                        bottom = 8.dp
                    )
                    .background(Color.Transparent)
            )
            Text(
                text = searchTerm,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier
                    .padding(
                        start = 0.dp,
                        top = 8.dp,
                        end = 0.dp,
                        bottom = 8.dp
                    )
                    .background(Color.Transparent)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DetailsWindow(
    modifier: Modifier = Modifier,
    cocktail: Cocktail.Drink?,
    visibility: Boolean,
    gradientBg: Brush,
    closeAdditionalInfo: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically(
            initialOffsetY = {it}
        ),
        exit = slideOutVertically(
            targetOffsetY = {it}
        )
    )
    {
        Column(
            modifier
                .fillMaxWidth()
                .background(gradientBg)
                .clip(MaterialTheme.shapes.large)
                .clickable { closeAdditionalInfo() }
                .animateContentSize()
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    //"Nothing to show" text will display when empty placeholder is clicked
                    text = cocktail?.strDrink ?: "Nothing to show",
                    style = MaterialTheme.typography.h5,
                    modifier = modifier
                        .weight(10f)
                )
                Image(
                    painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                    modifier = modifier
                        .weight(1f),
                    contentDescription = "Image of cocktail"
                )
            }

            Text(
                text = cocktail?.strInstructions ?: "",
                style = MaterialTheme.typography.body1,
                modifier = modifier
                    .padding(8.dp)
            )

            Divider(
                modifier
                    .padding(24.dp, 8.dp, 24.dp, 8.dp),
                thickness = 1.dp
            )

            IngredientItem(cocktail?.strMeasure1, cocktail?.strIngredient1)
            IngredientItem(cocktail?.strMeasure2, cocktail?.strIngredient2)
            IngredientItem(cocktail?.strMeasure3, cocktail?.strIngredient3)
            IngredientItem(cocktail?.strMeasure4, cocktail?.strIngredient4)
            IngredientItem(cocktail?.strMeasure5, cocktail?.strIngredient5)
            IngredientItem(cocktail?.strMeasure6, cocktail?.strIngredient6)
            IngredientItem(cocktail?.strMeasure7, cocktail?.strIngredient7)
            IngredientItem(cocktail?.strMeasure8, cocktail?.strIngredient8)
            IngredientItem(cocktail?.strMeasure9, cocktail?.strIngredient9)
            IngredientItem(cocktail?.strMeasure10, cocktail?.strIngredient10)
            IngredientItem(cocktail?.strMeasure11, cocktail?.strIngredient11)
            IngredientItem(cocktail?.strMeasure12, cocktail?.strIngredient12)
            IngredientItem(cocktail?.strMeasure13, cocktail?.strIngredient13)
            IngredientItem(cocktail?.strMeasure14, cocktail?.strIngredient14)
            IngredientItem(cocktail?.strMeasure15, cocktail?.strIngredient15)
        }
    }
}

@Composable
fun IngredientItem(
    measure: String?,
    ingredient: String?,
    modifier: Modifier = Modifier
) {
    if (measure.isNullOrBlank() && ingredient.isNullOrBlank()) {
        //don't compose the empty row
    } else {
        Row(
            modifier
                .padding(8.dp)
        ) {
            Text(
                text = measure.orEmpty(),
                style = MaterialTheme.typography.body2,
                modifier = modifier
                    .weight(2.5f)
            )

            Text(
                text = ingredient.orEmpty(),
                style = MaterialTheme.typography.body2,
                modifier = modifier
                    .weight(5f)
                    .padding(4.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    }
}