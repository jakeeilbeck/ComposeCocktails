package com.example.composecocktails.ui.screens.favourites


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.composecocktails.data.models.Cocktail
import com.google.accompanist.coil.rememberCoilPainter

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Favourites(
    viewModel: FavouritesViewModel
) {

    val favourites = viewModel.favouriteCocktails.observeAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (!favourites.value.isNullOrEmpty()) {
            items(favourites.value!!) { cocktail ->
                FavouriteListItem(
                    cocktail = cocktail,
                    deleteCocktail = { viewModel.deleteCocktail(it) }
                )
            }
        }else{
            //TODO show no favourites message
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun FavouriteListItem(
    modifier: Modifier = Modifier,
    cocktail: Cocktail.Drink,
    deleteCocktail: (String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                deleteCocktail(cocktail.idDrink)
            },
        elevation = 2.dp
    ) {
        Row {
            ListItem(
                icon = {
                    Image(
                        painter = rememberCoilPainter(
                            request = cocktail.strDrinkThumb,
                            fadeIn = true
                        ),
                        modifier = modifier
                            .size(64.dp)
                            .clip(shape = MaterialTheme.shapes.small),
                        contentDescription = null
                    )
                },
                text = { Text(text = cocktail.strDrink.toString()) },
                secondaryText = { Text(text = cocktail.strCategory.toString()) }
            )
        }
    }
}