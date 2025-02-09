package com.example.fotleague.models

import kotlinx.serialization.Serializable

@Serializable
class GameweekScore (
    val gameweek: Int,
    val score: Int?
)