package com.tracker.stocktracker.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.tracker.stocktracker.R
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.ui.viewmodel.StockTrackerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowComponentsToolBar(
    title: String,
    onBackClicked: () -> Unit,
    viewModel: StockTrackerViewModel = koinViewModel()
) {
    val toolbarState by viewModel.viewState.collectAsState()

    CenterAlignedTopAppBar(
        title = { Text(title, fontWeight = Bold) },
        navigationIcon = {
            ShowNavigation(toolbarState.connectionStatus, onBackClicked)
        },
        actions = {
            ShowActionButton(
                toggleStatus = toolbarState.toggleStatus,
                onClick = { viewModel.onEvent(StockXEvent.ToggleStartStop) }
            )
        }
    )
}

@Composable
private fun ShowNavigation(connectionStatus: Boolean?, onBackClicked: () -> Unit) {
    IconButton(onClick = onBackClicked) {
        Image(
            if (connectionStatus == true)
                painterResource(R.drawable.ic_green_light)
            else painterResource(R.drawable.ic_red_light), contentDescription = null
        )
    }
}

@Composable
private fun ShowActionButton(toggleStatus: Boolean, onClick: () -> Unit) {
    Text(
        text = if (toggleStatus) "Stop" else "Start",
        modifier = Modifier.clickable { onClick() }
    )
}
