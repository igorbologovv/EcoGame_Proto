package com.example.econbeacon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.econbeacon.viewmodel.GameStateViewModel
import com.example.econbeacon.viewmodel.MarketItem

@Composable
fun TradeDialog(
    item: MarketItem,
    gameStateViewModel: GameStateViewModel,
    isBuying: Boolean,         // true = buy, false = sell
    onDismiss: () -> Unit
) {
    // Context for saving json
    val context = LocalContext.current

    // Remember entered amount
    var quantity by remember { mutableStateOf("") }

    // Get current state from ViewModel
    val gameState = gameStateViewModel.gameState.collectAsState().value
    val balance = gameState?.balance ?: 0.0
    val resources = gameState?.resources ?: emptyMap()

    // Count and check if the operation is legit
    val qtyInt = quantity.toIntOrNull() ?: 0
    val totalCost = item.price * qtyInt
    val hasEnoughBalance = (balance >= totalCost)
    val hasEnoughResources = (resources[item.name] ?: 0) >= qtyInt
    //Similar logic for the avatar pickerDialog in main screen with lambda
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isBuying) "Buy ${item.name}" else "Sell ${item.name}")
        },
        text = {
            Column {
                Text("Price per unit: ${item.price} USD")

                // amount field
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { newVal ->
                        // only digits are allowed
                        quantity = newVal.filter { it.isDigit() }
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Total cost: $totalCost USD")

                // Not enough money Alert
                if (isBuying && !hasEnoughBalance) {
                    Text("Not enough balance!", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // If the amount is less or eq 0 = exit
                    if (qtyInt <= 0) return@Button

                    // Buy
                    if (isBuying) {
                        if (hasEnoughBalance) {
                            gameStateViewModel.changeBalance(-totalCost)
                            gameStateViewModel.addResource(item.name, qtyInt)
                            gameStateViewModel.saveGameState(context)
                            onDismiss()
                        }
                    }
                    // Sell
                    else {
                        if (hasEnoughResources) {
                            gameStateViewModel.changeBalance(+totalCost)
                            gameStateViewModel.removeResource(item.name, qtyInt)
                            gameStateViewModel.saveGameState(context)
                            onDismiss()
                        }
                    }
                },
                enabled = when {
                    isBuying -> hasEnoughBalance && qtyInt > 0
                    else -> hasEnoughResources && qtyInt > 0
                }
            ) {
                Text(if (isBuying) "Buy" else "Sell")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
