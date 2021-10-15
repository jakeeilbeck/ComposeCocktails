package com.example.composecocktails.ui.screens.favourites

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.composecocktails.Screens
import com.example.composecocktails.ui.screens.CocktailListItem
import com.example.composecocktails.ui.screens.DetailsWindow
import com.example.composecocktails.ui.screens.ErrorBanner
import com.example.composecocktails.ui.screens.SharedViewModel
import com.example.composecocktails.ui.theme.gradientBackground
import com.example.composecocktails.ui.theme.gradientBlueBackground
import com.example.composecocktails.ui.theme.gradientDetailsSearch
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Favourites(
    viewModel: SharedViewModel,
    listState: LazyListState
) {

    val systemUiController = rememberSystemUiController()
    val favourites = viewModel.favouriteCocktails.observeAsState()
    val showDetails = viewModel.showAdditionalInfoFavourites
    val cocktailInfo = viewModel.cocktailAdditionalInfoFavourites
    val halfScreenHeight = LocalContext.current.resources.displayMetrics
        .run { heightPixels / density }.toInt() / 2
    val statusBarColour = MaterialTheme.colors.surface

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColour)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground())
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState
        ) {

            if (!favourites.value.isNullOrEmpty()) {
                items(favourites.value!!) { cocktail ->
                    CocktailListItem(
                        cocktail = cocktail,
                        showInfo = {
                            viewModel.updateAdditionalInfo(it, Screens.Favourites.title)
                        },
                        updateFavourite = { viewModel.updateFavourite(it) }
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.Center)) {
            ErrorBanner(
                errorText = "No favourites to show",
                visibility = favourites.value.isNullOrEmpty(),
                gradient = gradientBlueBackground()
            )
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
                    viewModel.updateAdditionalInfo(cocktailInfo, Screens.Favourites.title)
                }
            }
        }
    }
}