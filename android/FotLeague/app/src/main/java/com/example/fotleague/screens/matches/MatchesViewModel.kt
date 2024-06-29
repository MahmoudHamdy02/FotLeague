package com.example.fotleague.screens.matches

import androidx.lifecycle.ViewModel
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(private val api: FotLeagueApi): ViewModel() {


}