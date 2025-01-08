package com.example.fotleague.data

import com.example.fotleague.models.League
import com.example.fotleague.models.LeagueUser
import com.example.fotleague.models.Match
import com.example.fotleague.models.Prediction
import com.example.fotleague.models.Score
import com.example.fotleague.models.Status
import com.example.fotleague.models.User
import com.example.fotleague.models.UserScore
import com.example.fotleague.models.network.request.AddOrEditPredictionRequest
import com.example.fotleague.models.network.request.CreateLeagueRequest
import com.example.fotleague.models.network.request.JoinLeagueRequest
import com.example.fotleague.models.network.request.LeaveLeagueRequest
import com.example.fotleague.models.network.request.LoginRequest
import com.example.fotleague.models.network.request.SignUpRequest
import com.example.fotleague.models.network.response.GetLeagueDetailsResponse
import com.example.fotleague.models.network.response.LogoutResponse
import com.example.fotleague.models.network.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FotLeagueApi {

    @GET("/")
    suspend fun getStatus(): Status

    @GET("/auth/status")
    suspend fun getAuthUser(): Response<User>

    @GET("/matches/{season}")
    suspend fun getMatches(@Path("season") season: Int): Response<List<Match>>


    @GET("/predictions/")
    suspend fun getPredictions(): Response<List<Prediction>>

    @POST("/predictions")
    suspend fun addPrediction(@Body body: AddOrEditPredictionRequest): Response<Prediction>

    @PATCH("/predictions")
    suspend fun updatePrediction(@Body body: AddOrEditPredictionRequest): Response<Prediction>


    @GET("/scores/user")
    suspend fun getUserScores(): Response<List<Score>>

    @GET("/scores/global")
    suspend fun getGlobalScores(@Query("num") num: Int): Response<List<UserScore>>


    @GET("/leagues/user/leagues")
    suspend fun getLeagues(): Response<List<League>>

    @GET("/leagues/{id}")
    suspend fun getLeagueDetails(@Path("id") id: Int): Response<GetLeagueDetailsResponse>

    @POST("/leagues")
    suspend fun createLeague(@Body body: CreateLeagueRequest): Response<League>

    @POST("/leagues/join")
    suspend fun joinLeague(@Body body: JoinLeagueRequest): Response<Unit>

    @POST("/leagues/leave")
    suspend fun leaveLeague(@Body body: LeaveLeagueRequest): Response<LeagueUser>

    @DELETE("/leagues/{id}")
    suspend fun deleteLeague(@Path("id") id: Int): Response<League>


    @POST("/auth/signup")
    suspend fun signUp(@Body body: SignUpRequest): Response<SignUpResponse>

    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<User>

    @POST("/auth/logout")
    suspend fun logout(): Response<LogoutResponse>
}
