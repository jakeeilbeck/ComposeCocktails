package com.example.composecocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.composecocktails.ui.screens.favourites.Favourites
import com.example.composecocktails.ui.screens.favourites.FavouritesViewModel
import com.example.composecocktails.ui.screens.home.Home
import com.example.composecocktails.ui.theme.*
import com.example.composecocktails.ui.screens.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel by viewModels<HomeViewModel>()
        val favouritesViewModel by viewModels<FavouritesViewModel>()

        setContent {
            ComposeCocktailsTheme {
                Surface {
                    Home(homeViewModel)
//                    Favourites(favouritesViewModel)
                }
            }
        }
    }
}