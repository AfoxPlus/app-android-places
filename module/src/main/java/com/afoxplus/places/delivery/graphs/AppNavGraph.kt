package com.afoxplus.places.delivery.graphs

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.afoxplus.places.delivery.screens.AutocompleteScreen
import com.afoxplus.places.delivery.screens.MapScreen

@Composable
fun AppNavGraph(activity: Activity, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Graph.Map.route) {
        composable(route = Graph.Map.route) {
            MapScreen(activity = activity, navController = navController) {
                navController.navigate(Graph.Autocomplete.route)
            }
        }
        composable(route = Graph.Autocomplete.route) {
            AutocompleteScreen(navController = navController) { establishment ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "location_result",
                    establishment.location
                )
                navController.popBackStack()
            }
        }
    }
}

sealed class Graph(val route: String) {
    object Map : Graph("map")
    object Autocomplete : Graph("autocomplete")
}