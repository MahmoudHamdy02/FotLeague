package com.example.fotleague.models.network.request

import kotlinx.serialization.Serializable

@Serializable
data class RenameLeagueRequest(
    val leagueId: Int,
    val name: String
)