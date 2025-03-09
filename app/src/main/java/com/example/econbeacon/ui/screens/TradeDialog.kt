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
    isBuying: Boolean,         // true = покупка, false = продажа
    onDismiss: () -> Unit
) {
    // Контекст для сохранения в JSON
    val context = LocalContext.current

    // Локальное состояние для поля ввода количества
    var quantity by remember { mutableStateOf("") }

    // Достаём текущее состояние из ViewModel
    val gameState = gameStateViewModel.gameState.collectAsState().value
    val balance = gameState?.balance ?: 0.0
    val resources = gameState?.resources ?: emptyMap()

    // Считаем итоговую стоимость и проверяем, хватает ли денег / ресурсов
    val qtyInt = quantity.toIntOrNull() ?: 0
    val totalCost = item.price * qtyInt
    val hasEnoughBalance = (balance >= totalCost)
    val hasEnoughResources = (resources[item.name] ?: 0) >= qtyInt

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

                    // Покупка
                    if (isBuying) {
                        if (hasEnoughBalance) {
                            gameStateViewModel.changeBalance(-totalCost)
                            gameStateViewModel.addResource(item.name, qtyInt)
                            gameStateViewModel.saveGameState(context)
                            onDismiss()
                        }
                    }
                    // Продажа
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
