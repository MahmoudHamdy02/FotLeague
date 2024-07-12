package com.example.fotleague.models.network.response

import com.example.fotleague.models.League
import com.example.fotleague.models.UserScore
import kotlinx.serialization.Serializable

@Serializable
data class GetLeagueDetailsResponse(
    val league: League,
    val userScores: List<UserScore>
)
