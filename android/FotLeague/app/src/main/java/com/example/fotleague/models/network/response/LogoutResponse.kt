package com.example.fotleague.models.network.response

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val message: String
)