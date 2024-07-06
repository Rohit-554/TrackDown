package io.jadu.trackdown.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.jadu.trackdown.presentation.details.DetailsScreen
import io.jadu.trackdown.presentation.companyList.StocksApp

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.StockApp.route) {
        composable(route = Screen.StockApp.route) {
            StocksApp(navController = navController)
        }
        composable(
            route = Screen.CompanyDetail.route + "/{symbol}",
            arguments = listOf(navArgument("symbol") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { entry->
            DetailsScreen(symbol = entry.arguments?.getString("symbol") ?: "")
        }
    }
}