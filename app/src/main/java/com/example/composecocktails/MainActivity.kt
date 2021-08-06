package com.example.composecocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                    BottomNav(
                        homeViewModel = homeViewModel,
                        favouritesViewModel = favouritesViewModel
                    )
                }
            }
        }
    }
}

//https://developer.android.com/jetpack/compose/navigation#bottom-nav
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun BottomNav(homeViewModel: HomeViewModel, favouritesViewModel: FavouritesViewModel) {
    val items = listOf(
        Screens.Home,
        Screens.Favourites
    )
    val navController = rememberNavController()
    val listState = rememberLazyListState()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = "",
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screens.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screens.Home.route) { Home(homeViewModel, listState) }
            composable(Screens.Favourites.route) { Favourites(favouritesViewModel) }
        }
    }
}

sealed class Screens(val icon: ImageVector, val title: String, val route: String) {
    object Home : Screens(Icons.Filled.Home, "Home", "home")
    object Favourites : Screens(Icons.Filled.Favorite, "Favourites", "favourites")
}