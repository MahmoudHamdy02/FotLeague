package com.example.fotleague.screens.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(private val api: FotLeagueApi?) : ViewModel() {

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { state -> state.copy(isLoading = true) }

            if (api == null) {
                _state.update { state -> state.copy(isLoading = false, error = "Failed to connect to server") }
            } else {
                try {
                    val matches = api.getMatches(2025)
                    val body = matches.body()
                    if (matches.isSuccessful && body != null) {
                        _state.update { state -> state.copy(matches = body, isLoading = false) }
                    }
                } catch (e: Exception) {
                    _state.update { state -> state.copy(error = if (e is ConnectException) "Failed to connect to server" else "An error occurred", isLoading = false) }
                }
            }

        }
    }

    fun onEvent(event: MatchesEvent) {
        when (event) {
            MatchesEvent.CloseDialog -> _state.update { state -> state.copy(predictionDialogOpen = false) }
            MatchesEvent.OpenDialog -> _state.update { state -> state.copy(predictionDialogOpen = true) }
            is MatchesEvent.SelectMatch -> _state.update { state -> state.copy(selectedMatch = event.match) }
        }
    }
}

data class MatchesState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val matches: List<Match> = emptyList(),
    val predictionDialogOpen: Boolean = false,
    val selectedMatch: Match = Match(0, "", "", 0, 0, 0, "", 0, 0)
)

sealed interface MatchesEvent {
    data object OpenDialog : MatchesEvent
    data object CloseDialog : MatchesEvent
    data class SelectMatch(val match: Match) : MatchesEvent
}