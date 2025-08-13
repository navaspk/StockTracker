package com.tracker.stocktracker.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tracker.base.model.PriceInfo
import com.tracker.stocktracker.ui.theme.font12Body
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tracker.stocktracker.R
import com.tracker.stocktracker.ui.theme.Body16Medium
import com.tracker.stocktracker.ui.viewmodel.StockTrackerViewModel
import com.tracker.stocktracker.utils.epochToDateTime

/**
 * Home screen build using LazyColumn and helps to show Stock symbols, price, status price and date of modification
 *
 * Created by : Navas
 * Date : 12/08/2025
 */
@Composable
fun StockTrackerHomeScreen(
    viewModel: StockTrackerViewModel = koinViewModel()
) {

    val prices by viewModel.prices.collectAsState()

    LazyColumn {
        items(prices, key = { it.symbol }) { item ->
            StockRow(item)
            Divider()
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StockRow(info: PriceInfo) {
    val diff = info.price - info.previousPrice
    val up = diff >= 0.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(font12Body.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                info.symbol,
                style = Body16Medium
            )
            Text(
                text = stringResource(R.string.stock_x_updated, epochToDateTime(info.timestamp)),
                fontSize = 12.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                String.format("%.2f", info.price),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = if (up) painterResource(id = R.drawable.ic_arrow_up) else painterResource(
                        R.drawable.ic_arrow_down
                    ),
                    contentDescription = null
                )
                Text((if (up) "+" else "-") + String.format("%.2f", kotlin.math.abs(diff)))
            }
        }
    }
}
