package com.example.fotleague.data

import com.example.fotleague.models.Match
import com.example.fotleague.models.Status
import retrofit2.http.GET
import retrofit2.http.Path

interface FotLeagueApi {

    @GET("/")
    suspend fun getStatus(): Status

    @GET("/matches/{season}")
    suspend fun getMatches(@Path("season") season: Int): List<Match>
}
