package com.example.econbeacon.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.econbeacon.data.GameStateData
import com.example.econbeacon.data.GameStateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



/*
*
* GameStateViewModel is a representational model which keeps game state
* Updates balance and  the amount of resources
* and reads data from json and updates the state*/
class GameStateViewModel : ViewModel() {

    // _gamestate keeps the data dynamically
    private val _gameState = MutableStateFlow<GameStateData?>(null)

    //read only
    val gameState = _gameState.asStateFlow()

    /**
     * Init game
     * Load data from json
     */
    fun initGameState(context: Context) {
        //load data and update the state
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
     * Add/reduce balance when buying/selling something
     *  Since GameStateData is immutable, we must use copy() to create a new instance with updated values.
     * This ensures StateFlow detects changes and updates the UI, as modifying the existing object is not possible.
     *
     */
    fun changeBalance(delta: Double) {
        val current = _gameState.value ?: return
        _gameState.value = current.copy(balance = current.balance + delta)
    }

    /**
     * Add/Delete resource amount when operating with some resource
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


