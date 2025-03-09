package com.example.econbeacon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.econbeacon.R
import com.example.econbeacon.viewmodel.MarketItem
import com.example.econbeacon.viewmodel.MarketViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.econbeacon.viewmodel.GameStateViewModel

val MedievalButtonColor = Color(0xFF6D4C41)
val MedievalTextColor = Color(0xFFFFD700)

@Composable
fun PlayerStatus(gameStateViewModel: GameStateViewModel) {
    // Collect the current game state from the view model
    val gameState by gameStateViewModel.gameState.collectAsState()
    gameState?.let { state ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display current balance
            Text(text = "Balance: ${state.balance}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            // Display each resource and its amount
            state.resources.forEach { (resource, amount) ->
                Text(text = "$resource: $amount", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MarketScreen(
    marketViewModel: MarketViewModel,
    // Pass GameStateViewModel to manage player's balance and resources
    gameStateViewModel: GameStateViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // List of market items from MarketViewModel
    val items by marketViewModel.items.collectAsState()

    // State for showing trade dialog and selected item
    var showTradeDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<MarketItem?>(null) }
    var isBuying by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.market_back),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display player status: balance and resources
            PlayerStatus(gameStateViewModel = gameStateViewModel)

            // Header row with screen title and "Back" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Marketplace",
                    fontFamily = MedievalFont,
                    fontSize = 32.sp
                )

                Button(onClick = { onBack() }, colors = ButtonDefaults.buttonColors(
                    containerColor = MedievalButtonColor,
                    contentColor = MedievalTextColor
                )) {
                    Text(
                        text = "Back",
                        fontFamily = MedievalFont,
                        fontSize = 24.sp
                    )
                }
            }

            // List of market items
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items) { item ->
                    MarketRow(
                        item = item,
                        // On Buy button click
                        onBuy = { clickedItem ->
                            selectedItem = clickedItem
                            isBuying = true
                            showTradeDialog = true
                        },
                        // On Sell button click
                        onSell = { clickedItem ->
                            selectedItem = clickedItem
                            isBuying = false
                            showTradeDialog = true
                        }
                    )
                    HorizontalDivider() // Visual divider between items
                }
            }

            // "Refresh Prices" button
            Button(
                onClick = {
                    coroutineScope.launch {
                        marketViewModel.fetchPrices()
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedievalButtonColor,
                    contentColor = MedievalTextColor
                )
            ) {
                Text(
                    text = "Refresh Prices",
                    fontFamily = MedievalFont,
                    fontSize = 24.sp
                )
            }
        }
    }

    // Show trade dialog if an item is selected
    if (showTradeDialog && selectedItem != null) {
        TradeDialog(
            item = selectedItem!!,
            gameStateViewModel = gameStateViewModel,
            isBuying = isBuying,
            onDismiss = { showTradeDialog = false }
        )
    }
}
@Composable
fun MarketRow(
    item: MarketItem,
    onBuy: (MarketItem) -> Unit,
    onSell: (MarketItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name)
        Text(text = "${item.price} IB")

        Row {
            // buy button
            Button(
                onClick = { onBuy(item) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedievalButtonColor,
                    contentColor = MedievalTextColor
                )
            ) {
                Text(
                    text = "Buy",
                    fontFamily = MedievalFont,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
           // Sell button
            Button(
                onClick = { onSell(item) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedievalButtonColor,
                    contentColor = MedievalTextColor
                )
            ) {
                Text(
                    text = "Sell",
                    fontFamily = MedievalFont,
                    fontSize = 18.sp
                )
            }
        }
    }
}
