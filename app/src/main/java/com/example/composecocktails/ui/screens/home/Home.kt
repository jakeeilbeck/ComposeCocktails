package com.example.composecocktails.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.composecocktails.Screens
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.ui.screens.*
import com.example.composecocktails.ui.theme.gradientBackground
import com.example.composecocktails.ui.theme.gradientDetailsSearch
import com.example.composecocktails.ui.theme.gradientHeader
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
    viewModel: SharedViewModel,
    listState: LazyListState
) {

    val randomCocktails = viewModel.randomCocktailList
    val systemUiController = rememberSystemUiController()
    val showDetails = viewModel.showAdditionalInfoHome
    val searchedCocktails = viewModel.searchedCocktailList
    val cocktailInfo = viewModel.cocktailAdditionalInfoHome
    val searchTerm = viewModel.searchTerm
    val errorMessageSearchTerm = viewModel.errorMessageSearchTerm.collectAsState().value
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
                    .background(brush = gradientBackground()),
                state = listState
            ) {

                item {
                    Header(
                        "Why not try...",
                        gradientHeader()
                    )
                }

                item {
                    Carousel(randomCocktails) {
                        viewModel.updateAdditionalInfo(it, Screens.Home.title)
                    }
                }

                stickyHeader {
                    SearchBar(
                        gradientBg = gradientDetailsSearch(),
                        searchOption = {
                            viewModel.searchOption = it
                        },
                        searchCocktail = {
                            viewModel.searchCocktail(it)
                        },
                        searchQuery = searchTerm
                    )
                }

                item {
                    ErrorBanner(
                        errorText = errorType.errorMessage,
                        //show the not found cocktail name in the error message
                        searchTerm = if (errorType == ErrorType.NoResult) {
                            errorMessageSearchTerm
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
                                    viewModel.updateAdditionalInfo(it, Screens.Home.title)
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
                        cocktail = cocktailInfo,
                        visibility = showDetails.value,
                        gradientBg = gradientDetailsSearch()
                    ) {
                        viewModel.updateAdditionalInfo(cocktailInfo, Screens.Home.title)
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
                painter = rememberImagePainter(
                    data = randomCocktail?.strDrinkThumb,
                    builder = { crossfade(true) }
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
    searchOption: (String) -> Unit,
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
            modifier.fillMaxWidth(),
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
                    text = "Search cocktail or ingredient...",
                    style = MaterialTheme.typography.subtitle2,
                    fontStyle = FontStyle.Italic
                )
            },
            trailingIcon = { SearchOptions{searchOption(it)} },
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

@Composable
fun SearchOptions(
    searchOption: (String) -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    var searchType by remember { mutableStateOf("Cocktail")}
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(Color.Transparent)
            .clickable { expanded = true }
            .fillMaxHeight()
            .height(56.dp)
    ) {
        Text(
            text = searchType,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterVertically)
        )

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .background(gradientHeader())
            .width(120.dp)
    ) {

        DropdownMenuItem(
            onClick = {
                searchType = "Cocktail"
                expanded = false
                searchOption(searchType)
            },
        ) {
            MenuItemText("Cocktail")
        }

        DropdownMenuItem(
            onClick = {
                searchType = "Ingredient"
                expanded = false
                searchOption(searchType)
            },
        ) {
            MenuItemText("Ingredient")
        }
    }
}

@Composable
fun MenuItemText(itemText: String){
    Text(
        text = itemText,
        fontSize = 16.sp,
        color = LocalContentColor.current.copy(alpha = 0.75f)
    )
}