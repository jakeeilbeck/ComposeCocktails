package com.example.composecocktails.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.ui.theme.getGradientBackground
import com.example.composecocktails.ui.theme.getGradientDetailsSearch
import com.example.composecocktails.ui.theme.getGradientErrorBackground
import com.example.composecocktails.ui.theme.getGradientHeader
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Home(
    viewModel: HomeViewModel
) {

    val randomCocktails = viewModel.randomCocktailList
    val systemUiController = rememberSystemUiController()
    val showDetails = rememberSaveable { (mutableStateOf(false)) }
    val searchedCocktails = viewModel.searchedCocktailList
    val info = viewModel.cocktailAdditionalInfo
    val searchTerm = viewModel.searchTerm
    val errorType = viewModel.errorType
    val halfScreenHeight = LocalContext.current.resources.displayMetrics
        .run { heightPixels / density }.toInt() / 2
    val gradientDetailsSearch = getGradientDetailsSearch()
    val gradientHeader = getGradientHeader()
    val gradientBackground = getGradientBackground()
    val statusBarColour = MaterialTheme.colors.surface

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColour)
    }

    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBackground)
        ) {
            item {
                Header(
                    "Why not try...",
                    gradientHeader
                )
            }

            item {
                Carousel(randomCocktails) {
                    viewModel.cocktailAdditionalInfo = it
                    showDetails.value = true
                }
            }

            stickyHeader {
                SearchBar(
                    gradientBg = gradientDetailsSearch,
                    searchCocktail = {
                        viewModel.searchCocktail(it)
                    })
            }

            item {
                ErrorBanner(
                    errorText = when (errorType.value) {
                        is ErrorTypes.NoResult -> "No results for: "
                        is ErrorTypes.NoConnection -> "No internet connection"
                        is ErrorTypes.NoError -> ""
                    },
                    searchTerm = if (errorType.value == ErrorTypes.NoResult) {
                        searchTerm.value
                    } else {
                        ""
                    },
                    visibility = errorType.value == ErrorTypes.NoResult || errorType.value == ErrorTypes.NoConnection
                )
            }

            if (!searchedCocktails.isNullOrEmpty()) {
                items(searchedCocktails) { cocktail ->
                    SearchListItem(
                        searchedCocktail = cocktail,
                        showInfo = {
                            viewModel.cocktailAdditionalInfo = it
                            showDetails.value = true
                        })
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(MaterialTheme.shapes.large)
                .heightIn(0.dp, halfScreenHeight.dp)
        ) {
            item {
                DetailsWindow(
                    cocktail = info,
                    visibility = showDetails.value,
                    gradientBg = gradientDetailsSearch
                ) {
                    showDetails.value = false
                }
            }
        }
    }
}

@Composable
fun Header(
    text: String,
    gradientBg: Brush,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .background(brush = gradientBg)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.Transparent)
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun Carousel(
    randomCocktails: List<Cocktail.Drink?>,
    showInfo: (Cocktail.Drink?) -> Unit
) {
    Surface(color = MaterialTheme.colors.primaryVariant) {
        LazyRow {
            items(randomCocktails) { cocktail ->
                CarouselItem(randomCocktail = cocktail, showInfo = { showInfo(it) })
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    randomCocktail: Cocktail.Drink?,
    showInfo: (Cocktail.Drink?) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { showInfo(randomCocktail) },
        elevation = 2.dp
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = randomCocktail?.strDrinkThumb,
                    fadeIn = true
                ),
                modifier = modifier
                    .size(124.dp)
                    .clip(shape = MaterialTheme.shapes.small),
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    .width(124.dp),
                text = randomCocktail?.strDrink.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    gradientBg: Brush,
    modifier: Modifier = Modifier,
    searchCocktail: (String) -> Unit
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(brush = gradientBg)
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier.weight(5f),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.background
            ),
            label = {
                Text(
                    text = "Search cocktail...",
                    style = MaterialTheme.typography.subtitle2
                )
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                searchCocktail(searchQuery.value)
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun SearchListItem(
    modifier: Modifier = Modifier,
    searchedCocktail: Cocktail.Drink?,
    showInfo: (Cocktail.Drink?) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { showInfo(searchedCocktail) },
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp),
        ) {
            ListItem(
                icon = {
                    Image(
                        painter = rememberCoilPainter(
                            request = searchedCocktail?.strDrinkThumb,
                            fadeIn = true
                        ),
                        modifier = modifier
                            .size(64.dp)
                            .clip(shape = MaterialTheme.shapes.small)
                            .padding(0.dp)
                            .widthIn(0.dp),
                        contentDescription = null
                    )
                },
                text = { Text(text = searchedCocktail?.strDrink.toString()) },
                secondaryText = { Text(text = searchedCocktail?.strCategory.toString()) }
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
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically(
            initialOffsetY = { with(density) { -40.dp.roundToPx() } })
                + expandVertically(expandFrom = Alignment.Bottom)
                + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically()
                + shrinkVertically()
                + fadeOut()
    )
    {
        Column(
            modifier
                .fillMaxWidth()
                .background(gradientBg)
                .clip(MaterialTheme.shapes.large)
                .clickable { closeAdditionalInfo() }
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = cocktail?.strDrink.toString(),
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
                text = cocktail?.strInstructions.toString(),
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

@ExperimentalAnimationApi
@Composable
fun ErrorBanner(
    errorText: String,
    modifier: Modifier = Modifier,
    searchTerm: String = "",
    visibility: Boolean
) {
    val gradient = getGradientErrorBackground()
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