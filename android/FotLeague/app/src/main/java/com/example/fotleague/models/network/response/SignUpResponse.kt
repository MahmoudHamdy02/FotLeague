package com.example.fotleague.models.network.response

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val role: Int
)