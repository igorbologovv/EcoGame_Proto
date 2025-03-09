package com.example.econbeacon.data


import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


data class GameStateData(
    val balance: Double,
    val resources: Map<String, Int>
)

object GameStateRepository {

    private const val FILE_NAME = "gameState.json"

    /*
            loading json file with GameState,
            if file not found create a default values
     */
    fun loadGameState(context: Context): GameStateData {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {

            return GameStateData(
                balance = 1000.0, // Start balance
                resources = mapOf(
                    "Wood" to 10,
                    "Stone" to 10,
                    "Metal" to 10,
                    "Food" to 10
                )
            )
        }


        val json = file.readText()
        val type = object : TypeToken<GameStateData>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     Save current game state to the file
     */
    fun saveGameState(context: Context, gameStateData: GameStateData) {
        val file = File(context.filesDir, FILE_NAME)
        val json = Gson().toJson(gameStateData)
        file.writeText(json)
    }
}
