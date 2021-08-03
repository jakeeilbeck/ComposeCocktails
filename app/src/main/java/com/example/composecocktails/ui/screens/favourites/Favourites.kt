package com.example.composecocktails.ui.screens.favourites


import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composecocktails.ui.screens.CocktailListItem
import com.example.composecocktails.ui.screens.DetailsWindow
import com.example.composecocktails.ui.screens.ErrorBanner
import com.example.composecocktails.ui.theme.gradientBlueBackground
import com.example.composecocktails.ui.theme.gradientDetailsSearch


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Favourites(
    viewModel: FavouritesViewModel
) {

    val favourites = viewModel.favouriteCocktails.observeAsState()
    val showDetails = rememberSaveable { (mutableStateOf(false)) }
    val halfScreenHeight = LocalContext.current.resources.displayMetrics
        .run { heightPixels / density }.toInt() / 2

    Box{

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            if (!favourites.value.isNullOrEmpty()) {
                items(favourites.value!!) { cocktail ->
                    CocktailListItem(
                        cocktail = cocktail,
                        showInfo = {
                            viewModel.cocktailAdditionalInfo = it
                            showDetails.value = true
                        },
                        updateFavourite = { viewModel.deleteCocktail(it.idDrink) }
                    )
                }
            }

            item{
                ErrorBanner(
                    errorText = "No favourites to show",
                    visibility = favourites.value.isNullOrEmpty() ,
                    gradient = gradientBlueBackground())
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
                    cocktail = viewModel.cocktailAdditionalInfo,
                    visibility = showDetails.value,
                    gradientBg = gradientDetailsSearch()
                ) {
                    showDetails.value = false
                }
            }
        }
    }
}