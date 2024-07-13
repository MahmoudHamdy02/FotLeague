package com.example.fotleague.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.UserScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(private val api: FotLeagueApi, authStatus: AuthStatus) :
    ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authStatus.getAuthUser().collect {
                if (it != null) {
                    getScores()
                }
            }
        }
    }

    private suspend fun getScores() {
        val scoresResponse = api.getGlobalScores()
        val scoresBody = scoresResponse.body()
        if (scoresResponse.isSuccessful && scoresBody != null) {
            _state.update { state -> state.copy(scores = scoresBody) }
        }
    }

    fun onEvent(event: LeaderboardEvent) {

    }
}

data class LeaderboardState(
    val scores: List<UserScore> = emptyList()
)

sealed interface LeaderboardEvent {

}