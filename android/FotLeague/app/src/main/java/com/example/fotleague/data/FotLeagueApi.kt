package com.example.fotleague.data

import com.example.fotleague.models.Match
import com.example.fotleague.models.SignUpRequest
import com.example.fotleague.models.SignUpResponse
import com.example.fotleague.models.Status
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FotLeagueApi {

    @GET("/")
    suspend fun getStatus(): Status

    @GET("/matches/{season}")
    suspend fun getMatches(@Path("season") season: Int): Response<List<Match>>

    @POST("/auth/signup")
    suspend fun signUp(@Body body: SignUpRequest): Response<SignUpResponse>
}
