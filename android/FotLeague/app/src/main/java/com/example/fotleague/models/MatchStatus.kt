package com.example.fotleague.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class MatchStatus(val num: Int) {
    Upcoming(1),
    InProgress(2),
    Played(3),
    Aborted(4)
}