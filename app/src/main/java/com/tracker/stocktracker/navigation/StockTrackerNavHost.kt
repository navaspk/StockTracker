package com.tracker.stocktracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tracker.stocktracker.ui.composable.StockTrackerHomeScreen
import com.tracker.stocktracker.R

@Composable
fun StockTrackerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Routes.HOME,
    onTitleChange: (String) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            onTitleChange(stringResource(R.string.stock_x_home))
            StockTrackerHomeScreen()
        }
    }
}
