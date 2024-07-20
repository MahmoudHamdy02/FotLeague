package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Score(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("match_id")
    val matchId: Int,
    val score: Int
)
