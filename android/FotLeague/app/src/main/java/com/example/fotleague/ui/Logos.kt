package com.example.fotleague.ui

import com.example.fotleague.R

object Logos {
    private val logos = mapOf(
        "arsenal" to R.drawable.arsenal,
        "astonvilla" to R.drawable.astonvilla,
        "bournemouth" to R.drawable.bournemouth,
        "brentford" to R.drawable.brentford,
        "brighton" to R.drawable.brighton,
        "chelsea" to R.drawable.chelsea,
        "crystalpalace" to R.drawable.crystalpalace,
        "everton" to R.drawable.everton,
        "fulham" to R.drawable.fulham,
        "ipswich" to R.drawable.ipswich,
        "leicester" to R.drawable.leicester,
        "liverpool" to R.drawable.liverpool,
        "mancity" to R.drawable.mancity,
        "manunited" to R.drawable.manunited,
        "newcastle" to R.drawable.newcastle,
        "nottmforest" to R.drawable.nottmforest,
        "southampton" to R.drawable.southampton,
        "tottenham" to R.drawable.tottenham,
        "westham" to R.drawable.westham,
        "wolves" to R.drawable.wolves
    )

    fun getResourceId(teamName: String): Int {
        return logos[teamName.lowercase().replace(" ", "")] ?: R.drawable.shield
    }
}