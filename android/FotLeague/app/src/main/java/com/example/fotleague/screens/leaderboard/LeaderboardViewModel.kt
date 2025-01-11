package com.example.fotleague.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LeaderboardViewModel @Inject constructor(private val api: FotLeagueApi) :
    ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getScores(_state.value.numOfScores)
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
        }
    }
}

data class LeaderboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val scores: List<UserScore> = emptyList(),
    val isNumOfScoresDropdownExpanded: Boolean = false,
    val numOfScores: Int = 10
)

sealed interface LeaderboardEvent {
    data object ExpandNumOfScoresDropdown : LeaderboardEvent
    data object DismissNumOfScoresDropdown : LeaderboardEvent
    data class SelectNumOfScores(val num: Int) : LeaderboardEvent
}