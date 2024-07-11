package com.example.fotleague.models.network.request

import kotlinx.serialization.Serializable

@Serializable
data class JoinLeagueRequest(
    val code: String
)
