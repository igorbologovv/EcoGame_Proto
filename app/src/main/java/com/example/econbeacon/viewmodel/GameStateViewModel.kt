package com.example.econbeacon.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.econbeacon.data.GameStateData
import com.example.econbeacon.data.GameStateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameStateViewModel : ViewModel() {

    private val _gameState = MutableStateFlow<GameStateData?>(null)
    val gameState = _gameState.asStateFlow()

    /**
     * Init game
     * Load data from json
     */
    fun initGameState(context: Context) {
        viewModelScope.launch {
            val loadedState = GameStateRepository.loadGameState(context)
            _gameState.value = loadedState
        }
    }

    /**
     * Save current state
     */
    fun saveGameState(context: Context) {
        viewModelScope.launch {
            _gameState.value?.let { state ->
                GameStateRepository.saveGameState(context, state)
            }
        }
    }

    /**
     * Add/reduce balance
     */
    fun changeBalance(delta: Double) {
        val current = _gameState.value ?: return
        _gameState.value = current.copy(balance = current.balance + delta)
    }

    /**
     * Add/Delete resource
     */
    fun addResource(resourceName: String, quantity: Int) {
        val current = _gameState.value ?: return
        val oldQuantity = current.resources[resourceName] ?: 0
        val newMap = current.resources.toMutableMap()
        newMap[resourceName] = oldQuantity + quantity
        _gameState.value = current.copy(resources = newMap)
    }

    fun removeResource(resourceName: String, quantity: Int) {
        val current = _gameState.value ?: return
        val oldQuantity = current.resources[resourceName] ?: 0
        val newQuantity = (oldQuantity - quantity).coerceAtLeast(0)
        val newMap = current.resources.toMutableMap()
        newMap[resourceName] = newQuantity
        _gameState.value = current.copy(resources = newMap)
    }
}


