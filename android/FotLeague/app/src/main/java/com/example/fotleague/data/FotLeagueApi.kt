package com.example.fotleague.data

import com.example.fotleague.models.Status
import retrofit2.http.GET

interface FotLeagueApi {

    @GET("/")
    suspend fun getStatus(): Status
}
