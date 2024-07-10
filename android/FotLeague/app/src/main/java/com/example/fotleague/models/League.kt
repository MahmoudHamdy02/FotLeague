package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class League(
    val id: Int,
    val name: String,
    @SerialName("owner_id")
    val ownerId: Int,
    val code: String
)
