package com.example.fotleague.models

import kotlinx.serialization.Serializable

@Serializable
data class UserScore(
    val id: Int,
    val name: String,
    val score: Int
)
