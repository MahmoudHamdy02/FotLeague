package com.example.fotleague.data

import com.example.fotleague.models.Match
import com.example.fotleague.models.network.request.SignUpRequest
import com.example.fotleague.models.network.response.SignUpResponse
import com.example.fotleague.models.Status
import com.example.fotleague.models.User
import com.example.fotleague.models.network.request.LoginRequest
import com.example.fotleague.models.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface FotLeagueApi {

    @GET("/")
    suspend fun getStatus(): Status

    @GET("/auth/status")
    suspend fun getAuthUser(): Response<User>

    @GET("/matches/{season}")
    suspend fun getMatches(@Path("season") season: Int): Response<List<Match>>

    @POST("/auth/signup")
    suspend fun signUp(@Body body: SignUpRequest): Response<SignUpResponse>

    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}
