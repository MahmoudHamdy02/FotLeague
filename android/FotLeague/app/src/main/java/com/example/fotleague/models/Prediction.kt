package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prediction(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("match_id")
    val matchId: Int,
    val home: Int,
    val away: Int
)