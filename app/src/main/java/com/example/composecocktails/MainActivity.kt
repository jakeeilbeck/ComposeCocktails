package com.example.composecocktails

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import com.example.composecocktails.ui.screens.SharedViewModel
import com.example.composecocktails.ui.screens.createCocktail.CreateCocktail
import com.example.composecocktails.ui.screens.favourites.Favourites
import com.example.composecocktails.ui.screens.home.Home
import com.example.composecocktails.ui.theme.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedViewModel by viewModels<SharedViewModel>()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            ComposeCocktailsTheme {
                Surface {
                    BottomNav(
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}

//https://developer.android.com/jetpack/compose/navigation#bottom-nav
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun BottomNav(sharedViewModel: SharedViewModel) {
    val items = listOf(
        Screens.Home,
        Screens.Favourites,
        Screens.Create
    )
    val navController = rememberAnimatedNavController()
    val homeListState = rememberLazyListState()
    val favouritesListState = rememberLazyListState()
    val createListState = rememberLazyListState()

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
                                tint = screen.iconColour,
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            when(screen.route){
                                "favourites" -> {
                                    Screens.Favourites.iconColour = Color.Red
                                }
                                "home" -> {
                                    Screens.Favourites.iconColour = Color.LightGray
                                }
                                "create" -> {
                                    Screens.Favourites.iconColour = Color.LightGray
                                }
                            }

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
        AnimatedNavHost(
            navController,
            startDestination = Screens.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(
                Screens.Home.route,
                enterTransition = {_, _ ->
                    slideInHorizontally()
                },
                exitTransition = {_, _ ->
                    slideOutHorizontally()
                }
            ) {
                Home(sharedViewModel, homeListState)
            }
            composable(
                Screens.Favourites.route,
                enterTransition = {_, _ ->
                    slideInHorizontally()
                },
                exitTransition = {_, _ ->
                    slideOutHorizontally()
                }
            ) {
                Favourites(sharedViewModel, favouritesListState)
            }
            composable(
                Screens.Create.route,
                enterTransition = {_, _ ->
                    slideInHorizontally()
                },
                exitTransition = {_, _ ->
                    slideOutHorizontally()
                }
            ){
                CreateCocktail(sharedViewModel, createListState)
            }
        }
    }
}

sealed class Screens(val icon: ImageVector, var iconColour: Color, val title: String, val route: String) {
    object Home : Screens(Icons.Filled.Home, Color.LightGray,"Home", "home")
    object Favourites : Screens(Icons.Filled.Favorite, Color.LightGray,"Favourites", "favourites")
    object Create : Screens(Icons.Filled.Add, Color.LightGray,"Create", "create")
}