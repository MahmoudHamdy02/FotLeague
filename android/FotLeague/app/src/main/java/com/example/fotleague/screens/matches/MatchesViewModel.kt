package com.example.fotleague.screens.matches

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(private val api: FotLeagueApi) : ViewModel() {

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            val matches = api.getMatches(2025)
            Log.d("MATCHES", matches[0].home)
            Log.d("MATCHES", (matches[0].matchStatus == MatchStatus.Played.num).toString())
            _state.update { state -> state.copy(matches = matches) }
        }
    }
}

data class MatchesState(
    val matches: List<Match> = emptyList()
)