package com.example.fotleague.models.network.request

import kotlinx.serialization.Serializable

@Serializable
data class AddPredictionRequest(
    val matchId: Int,
    val home: Int,
    val away: Int
)