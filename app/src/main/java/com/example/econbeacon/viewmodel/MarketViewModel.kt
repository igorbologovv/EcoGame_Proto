package com.example.econbeacon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.econbeacon.data.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MarketItem(
    val name: String,
    val price: Double
)

class MarketViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<MarketItem>>(emptyList())
    val items: StateFlow<List<MarketItem>> = _items

    init {

        viewModelScope.launch {
            while (true) {
                fetchPrices()
                delay(10000)
            }
        }
    }

    suspend fun fetchPrices() {
        try {
            val response = RetrofitClient.marketApi.getPrices(
                ids = "matic-network,uniswap,chainlink,solana,cosmos",
                vs = "usd"
            )

            val newList = mutableListOf<MarketItem>()


            response["uniswap"]?.get("usd")?.let { uniswapPrice ->
                newList.add(MarketItem("Wood", uniswapPrice))
            }
            response["chainlink"]?.get("usd")?.let { chainlinkPrice ->
                newList.add(MarketItem("Stone", chainlinkPrice))
            }
            response["solana"]?.get("usd")?.let { solanaPrice ->
                newList.add(MarketItem("Metall", solanaPrice))
            }
            response["cosmos"]?.get("usd")?.let { cosmosPrice ->
                newList.add(MarketItem("Food", cosmosPrice))
            }

            _items.value = newList

        } catch (_: Exception) {

        }
    }

}
