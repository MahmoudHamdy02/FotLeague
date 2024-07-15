package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeagueUser(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("league_id")
    val leagueId: Int
)
