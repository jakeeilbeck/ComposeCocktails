package com.example.composecocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.composecocktails.ui.screens.home.Home
import com.example.composecocktails.ui.theme.*
import com.example.composecocktails.ui.screens.home.HomeViewModel


/*TODO fix app crashing without internet connection*/


class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cocktailViewModel by viewModels<HomeViewModel>()
        cocktailViewModel.getRandomCocktails()
        val randomCockTails = cocktailViewModel.randomCocktailList

        setContent {
            ComposeCocktailsTheme {
                Surface {
                    Home(randomCockTails, cocktailViewModel)
                }
            }
        }
    }
}