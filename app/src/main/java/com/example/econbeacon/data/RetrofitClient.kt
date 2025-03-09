package com.example.econbeacon.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val marketApi: MarketApi by lazy {
        retrofit.create(MarketApi::class.java)
    }
}
