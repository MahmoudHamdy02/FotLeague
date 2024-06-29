package com.example.fotleague.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitInstance {

    val api: FotLeagueApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.105:3001")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FotLeagueApi::class.java)
    }
}