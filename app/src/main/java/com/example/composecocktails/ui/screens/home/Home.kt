package com.example.composecocktails.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.ui.screens.CocktailListItem
import com.example.composecocktails.ui.screens.DetailsWindow
import com.example.composecocktails.ui.screens.ErrorBanner
import com.example.composecocktails.ui.theme.gradientBackground
import com.example.composecocktails.ui.theme.gradientDetailsSearch
import com.example.composecocktails.ui.theme.gradientHeader
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
    val gradientDetailsSearch = gradientDetailsSearch()
    val gradientHeader = gradientHeader()
    val gradientBackground = gradientBackground()
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
                        },
                        searchQuery = searchTerm)
                }

                item {
                    ErrorBanner(
                        errorText = errorType.errorMessage,
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
                            CocktailListItem(
                                cocktail = cocktail,
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
    searchCocktail: (String) -> Unit,
    searchQuery: MutableState<String>
) {
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
                searchQuery.value = searchQuery.value.trim()
                searchCocktail(searchQuery.value)
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
        )
    }
}