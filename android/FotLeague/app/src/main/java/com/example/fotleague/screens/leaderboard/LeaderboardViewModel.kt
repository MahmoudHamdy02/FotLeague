package com.example.fotleague.screens.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthStatus
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
class LeaderboardViewModel @Inject constructor(
    private val api: FotLeagueApi,
    val authStatus: AuthStatus
) :
    ViewModel() {

    private val _gameweekScoresState = MutableStateFlow(GameweekScoresState())
    val gameweekScoresState: StateFlow<GameweekScoresState> = _gameweekScoresState.asStateFlow()

    private val _scoresTableState = MutableStateFlow(ScoresTableState())
    val scoresTableState: StateFlow<ScoresTableState> = _scoresTableState.asStateFlow()

    val authState = authStatus.getAuthState()

    var userScores: List<GameweekScore> = emptyList()
    var highestScores: List<GameweekScore> = emptyList()
    var averageScores: List<GameweekScore> = emptyList()
    var currentGameweek: Int = 1

    init {
        viewModelScope.launch {
            getCurrentGameweek()
            _gameweekScoresState.update { it.copy(selectedGameweek = currentGameweek) }
            getGameweekScores()
            getGlobalScores(_scoresTableState.value.numOfScores)
            authState.collect {
                if (it.isLoggedIn)
                    getUserScores()
            }
        }
    }

    fun onEvent(event: LeaderboardEvent) {
        when (event) {
            LeaderboardEvent.DismissNumOfScoresDropdown -> _scoresTableState.update {
                it.copy(isNumOfScoresDropdownExpanded = false)
            }

            LeaderboardEvent.ExpandNumOfScoresDropdown -> _scoresTableState.update {
                it.copy(isNumOfScoresDropdownExpanded = true)
            }

            is LeaderboardEvent.SelectNumOfScores -> {
                _scoresTableState.update {
                    it.copy(
                        numOfScores = event.num,
                        isNumOfScoresDropdownExpanded = false
                    )
                }
                viewModelScope.launch {
                    _scoresTableState.update { it.copy(isLoading = true) }
                    getGlobalScores(event.num)
                }
            }

            LeaderboardEvent.Refresh -> {
                viewModelScope.launch {
                    _scoresTableState.update { state -> state.copy(isRefreshing = true) }
                    getGlobalScores(_scoresTableState.value.numOfScores)
                    getCurrentGameweek()
                    getUserScores()
                    getGameweekScores()
                    _scoresTableState.update { state -> state.copy(isRefreshing = false) }
                }
            }

            LeaderboardEvent.SelectNextGameweek -> {
                _gameweekScoresState.update {state ->
                    state.copy(
                        selectedGameweek = if (state.selectedGameweek < currentGameweek) state.selectedGameweek + 1 else state.selectedGameweek,
                        averageScore = averageScores.find { it.gameweek == state.selectedGameweek+1 }?.score,
                        highestScore = highestScores.find { it.gameweek == state.selectedGameweek+1 }?.score,
                        userScore = userScores.find { it.gameweek == state.selectedGameweek+1 }?.score,
                        isNextGameweekButtonEnabled = state.selectedGameweek+1 != currentGameweek,
                        isPrevGameweekButtonEnabled = true
                    )
                }
            }

            LeaderboardEvent.SelectPreviousGameweek -> {
                _gameweekScoresState.update { state ->
                    state.copy(
                        selectedGameweek = if (state.selectedGameweek > 1) state.selectedGameweek - 1 else 1,
                        averageScore = averageScores.find { it.gameweek == state.selectedGameweek-1 }?.score,
                        highestScore = highestScores.find { it.gameweek == state.selectedGameweek-1 }?.score,
                        userScore = userScores.find { it.gameweek == state.selectedGameweek-1 }?.score,
                        isPrevGameweekButtonEnabled = state.selectedGameweek-1 > 1,
                        isNextGameweekButtonEnabled = true,
                    )
                }
            }
        }
    }

    private suspend fun getCurrentGameweek() {
        val gameweekResponse = api.getCurrentGameweek()
        val gameweekBody = gameweekResponse.body()
        if (gameweekResponse.isSuccessful && gameweekBody != null) {
            currentGameweek = gameweekBody
        }
    }

    private suspend fun getGlobalScores(numOfScores: Int) {
        val scoresResponse = api.getGlobalScores(numOfScores)
        val scoresBody = scoresResponse.body()
        Log.d("SCORE", scoresBody?.get(0)?.score.toString())
        if (scoresResponse.isSuccessful && scoresBody != null) {
            _scoresTableState.update { state ->
                state.copy(
                    scores = scoresBody,
                    isLoading = false
                )
            }
        } else {
            _scoresTableState.update { state ->
                state.copy(
                    isLoading = false,
                    error = "An error occurred"
                )
            }
        }
    }

    private suspend fun getGameweekScores() {
        val maxScoresResponse = api.getHighestGameweekScores()
        val maxScoresBody = maxScoresResponse.body()
        val avgScoresResponse = api.getAverageGameweekScores()
        val avgScoresBody = avgScoresResponse.body()

        if (maxScoresResponse.isSuccessful && maxScoresBody != null &&
            avgScoresResponse.isSuccessful && avgScoresBody != null) {
            averageScores = avgScoresBody
            highestScores = maxScoresBody
            _gameweekScoresState.update { state ->
                state.copy(
                    averageScore = averageScores.find { it.gameweek == state.selectedGameweek }?.score,
                    highestScore = highestScores.find { it.gameweek == state.selectedGameweek }?.score,
                    isPrevGameweekButtonEnabled = currentGameweek != 1,
                    isLoading = false
                )
            }
        } else {
            _gameweekScoresState.update { state ->
                state.copy(
                    error = "An error occurred"
                )
            }
        }
    }

    private suspend fun getUserScores() {
        val scoresResponse = api.getUserGameweekScores()
        val scoresBody = scoresResponse.body()
        if (scoresResponse.isSuccessful && scoresBody != null) {
            userScores = scoresBody
            _gameweekScoresState.update { state -> state.copy(
                userScore = userScores.find { it.gameweek == state.selectedGameweek }?.score
            ) }
        }
    }
}

data class GameweekScoresState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userScore: Int? = null,
    val highestScore: Int? = null,
    val averageScore: Int? = null,
    val selectedGameweek: Int = 1,
    val isPrevGameweekButtonEnabled: Boolean = false,
    val isNextGameweekButtonEnabled: Boolean = false,
)

data class ScoresTableState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val scores: List<UserScore> = emptyList(),
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