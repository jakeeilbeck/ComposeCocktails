package com.example.composecocktails.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
    val errorType = when {
        viewModel.generalError.value != ErrorType.NoError -> {
            viewModel.generalError.value
        }
        viewModel.searchError.value != ErrorType.NoError -> {
            viewModel.searchError.value
        }
        else -> {
            ErrorType.NoError
        }
    }
    val halfScreenHeight = LocalContext.current.resources.displayMetrics
        .run { heightPixels / density }.toInt() / 2
    val gradientDetailsSearch = getGradientDetailsSearch()
    val gradientHeader = getGradientHeader()
    val gradientBackground = getGradientBackground()
    val statusBarColour = MaterialTheme.colors.surface
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColour)
    }

    Box {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = {
                viewModel.getRandomCocktails()
            },
            refreshTriggerDistance = 70.dp
        ) {

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
                        errorText = when (errorType) {
                            is ErrorType.NoResult -> "No results for: "
                            is ErrorType.NoConnection -> "No internet connection"
                            is ErrorType.OtherError -> "Error getting results"
                            is ErrorType.NoError -> ""
                        },
                        //show the not found cocktail name in the error message
                        searchTerm = if (errorType == ErrorType.NoResult) {
                            searchTerm.value
                        } else {
                            //otherwise don't append anything to the error message
                            ""
                        },
                        visibility = errorType != ErrorType.NoError
                    )
                }

                if (!searchedCocktails.isNullOrEmpty()) {
                    items(searchedCocktails) { cocktail ->
                        if (cocktail != null) {
                            SearchListItem(
                                searchedCocktail = cocktail,
                                showInfo = {
                                    viewModel.cocktailAdditionalInfo = it
                                    showDetails.value = true
                                },
                                updateFavourite = { viewModel.updateFavourite(it) }
                            )
                        }
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
    Surface(
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            //fixed size to stop carousel disappearing on recomposition
            .height(184.dp)
    ) {
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
                    .clip(shape = MaterialTheme.shapes.small)
                    .placeholder(
                        visible = randomCocktail == null,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    .width(124.dp)
                    .placeholder(
                        visible = randomCocktail == null,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
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

@SuppressLint("UnusedTransitionTargetStateParameter")
@ExperimentalMaterialApi
@Composable
fun SearchListItem(
    modifier: Modifier = Modifier,
    searchedCocktail: Cocktail.Drink,
    showInfo: (Cocktail.Drink?) -> Unit,
    updateFavourite: (Cocktail.Drink) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { showInfo(searchedCocktail) },
        elevation = 2.dp
    ) {
        Row {
            ListItem(
                icon = {
                    Image(
                        painter = rememberCoilPainter(
                            request = searchedCocktail.strDrinkThumb,
                            fadeIn = true
                        ),
                        modifier = modifier
                            .size(64.dp)
                            .clip(shape = MaterialTheme.shapes.small),
                        contentDescription = null
                    )
                },
                text = { Text(text = searchedCocktail.strDrink.toString()) },
                secondaryText = { Text(text = searchedCocktail.strCategory.toString()) },
                trailing =
                {
                    IconToggleButton(
                        checked = searchedCocktail.isFavourite,
                        onCheckedChange = {
                            updateFavourite(searchedCocktail)
                        }
                    ) {
                        //favourite click animation
                        val transition = updateTransition(
                            searchedCocktail.isFavourite,
                            label = "isFavourite indicator"
                        )

                        val tint by transition.animateColor(
                            label = "Tint"
                        ) {
                            if (searchedCocktail.isFavourite) Color.Red else Color.LightGray
                        }

                        val size by transition.animateDp(
                            transitionSpec = {
                                if (false isTransitioningTo true) {
                                    keyframes {
                                        durationMillis = 250
                                        35.dp at 0 with LinearOutSlowInEasing
                                        40.dp at 15 with FastOutLinearInEasing
                                        50.dp at 75
                                        42.dp at 150
                                    }
                                } else {
                                    spring(stiffness = Spring.StiffnessVeryLow)
                                }
                            },
                            label = "Size"
                        ) { 42.dp }

                        Icon(
                            imageVector = if (searchedCocktail.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = tint,
                            modifier = modifier
                                .size(size),
                        )
                    }
                }
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