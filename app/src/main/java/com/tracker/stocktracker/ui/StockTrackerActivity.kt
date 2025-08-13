package com.tracker.stocktracker.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.tracker.stocktracker.R
import com.tracker.stocktracker.navigation.StockTrackerNavHost
import com.tracker.stocktracker.ui.presentation.ShowMainToolBar
import com.tracker.stocktracker.ui.theme.StockTrackerTheme

class StockTrackerActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            StockTrackerTheme {
                Scaffold(
                    topBar = {
                        ShowMainToolBar(
                            stringResource(R.string.stock_x_home)
                        )
                    }
                ) { paddingValues ->
                    StockTrackerNavHost(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}
