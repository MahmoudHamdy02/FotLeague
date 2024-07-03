package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: Int,
    val home: String,
    val away: String,
    @SerialName("home_score")
    val homeScore: Int,
    @SerialName("away_score")
    val awayScore: Int,
    @SerialName("match_status")
    val matchStatus: Int,
    val datetime: String,
    val season: Int,
    val gameweek: Int
)