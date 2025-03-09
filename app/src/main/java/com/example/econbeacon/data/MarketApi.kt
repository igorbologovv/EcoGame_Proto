package com.example.econbeacon.data


import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApi {
    @GET("/api/v3/simple/price")
    suspend fun getPrices(
        @Query("ids") ids: String,           // "bitcoin,gold"
        @Query("vs_currencies") vs: String   // "usd"
    ): Map<String, Map<String, Double>>
}
