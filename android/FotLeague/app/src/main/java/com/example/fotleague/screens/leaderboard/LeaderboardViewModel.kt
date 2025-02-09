package com.example.fotleague.screens.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.GameweekScore
import com.example.fotleague.models.UserScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(private val api: FotLeagueApi) :
    ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getUserScores()
            getAverageScores()
            getHighestScores()
            getScores(_state.value.numOfScores)
        }
    }

    fun onEvent(event: LeaderboardEvent) {
        when (event) {
            LeaderboardEvent.DismissNumOfScoresDropdown -> _state.update {
                it.copy(isNumOfScoresDropdownExpanded = false)
            }

            LeaderboardEvent.ExpandNumOfScoresDropdown -> _state.update {
                it.copy(isNumOfScoresDropdownExpanded = true)
            }

            is LeaderboardEvent.SelectNumOfScores -> {
                _state.update {
                    it.copy(
                        numOfScores = event.num,
                        isNumOfScoresDropdownExpanded = false
                    )
                }
                viewModelScope.launch {
                    getScores(event.num)
                }
            }

            LeaderboardEvent.Refresh -> {
                viewModelScope.launch {
                    getUserScores()
                    getAverageScores()
                    getHighestScores()
                    getScores(_state.value.numOfScores)
                }
            }

            LeaderboardEvent.SelectNextGameweek -> {
                _state.update {
                    it.copy(
                        selectedGameweek = if (it.selectedGameweek < it.currentGameweek) it.selectedGameweek + 1 else it.selectedGameweek
                    )
                }
            }

            LeaderboardEvent.SelectPreviousGameweek -> {
                _state.update {
                    it.copy(
                        selectedGameweek = if (it.selectedGameweek > 1) it.selectedGameweek - 1 else 1
                    )
                }
            }
        }
    }

    private suspend fun getScores(numOfScores: Int) {
        try {
            val scoresResponse = api.getGlobalScores(numOfScores)
            val scoresBody = scoresResponse.body()
            if (scoresResponse.isSuccessful && scoresBody != null) {
                _state.update { state -> state.copy(scores = scoresBody) }
            }
        } catch (e: Exception) {
            _state.update { state ->
                state.copy(
                    error = "Failed to connect to server",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getUserScores() {
        val scoresResponse = api.getUserGameweekScores()
        val scoresBody = scoresResponse.body()
        if (scoresResponse.isSuccessful && scoresBody != null) {
            Log.d("USR", scoresBody.size.toString())
            _state.update { state -> state.copy(userGameweekScores = scoresBody) }
        }
    }

    private suspend fun getHighestScores() {
        val scoresResponse = api.getHighestGameweekScores()
        val scoresBody = scoresResponse.body()
        if (scoresResponse.isSuccessful && scoresBody != null) {
            Log.d("MAX", scoresBody.size.toString())
            val gw = scoresBody.filter { it.score != null }.size
            _state.update { state ->
                state.copy(
                    highestGameweekScores = scoresBody,
                    currentGameweek = gw,
                    selectedGameweek = gw,
                    isScoreLoading = false
                )
            }
        }
    }

    private suspend fun getAverageScores() {
        val scoresResponse = api.getAverageGameweekScores()
        val scoresBody = scoresResponse.body()
        if (scoresResponse.isSuccessful && scoresBody != null) {
            Log.d("AVG", scoresBody.size.toString())
            _state.update { state -> state.copy(averageGameweekScores = scoresBody) }
        }
    }
}

data class LeaderboardState(
    val isLoading: Boolean = false,
    val isScoreLoading: Boolean = true,
    val error: String? = null,
    val scores: List<UserScore> = emptyList(),
    val userGameweekScores: List<GameweekScore> = emptyList(),
    val highestGameweekScores: List<GameweekScore> = emptyList(),
    val averageGameweekScores: List<GameweekScore> = emptyList(),
    val selectedGameweek: Int = 1,
    val currentGameweek: Int = 1,
    val isNumOfScoresDropdownExpanded: Boolean = false,
    val numOfScores: Int = 10,
    val isRefreshing: Boolean = false
)

sealed interface LeaderboardEvent {
    data object ExpandNumOfScoresDropdown : LeaderboardEvent
    data object DismissNumOfScoresDropdown : LeaderboardEvent
    data class SelectNumOfScores(val num: Int) : LeaderboardEvent
    data object Refresh : LeaderboardEvent
    data object SelectNextGameweek : LeaderboardEvent
    data object SelectPreviousGameweek : LeaderboardEvent
}