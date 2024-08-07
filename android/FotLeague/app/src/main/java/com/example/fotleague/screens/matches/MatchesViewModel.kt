package com.example.fotleague.screens.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.Match
import com.example.fotleague.models.Prediction
import com.example.fotleague.models.Score
import com.example.fotleague.models.network.request.AddOrEditPredictionRequest
import com.example.fotleague.ui.components.picker.PickerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(private val api: FotLeagueApi) : ViewModel() {

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { state -> state.copy(isLoading = true) }


            try {
                getPredictions()
                getScores()
                getMatches()
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        error = if (e is ConnectException) "Failed to connect to server" else "An error occurred",
                        isLoading = false
                    )
                }
            }


        }
    }

    fun onEvent(event: MatchesEvent) {
        when (event) {
            MatchesEvent.CloseDialog -> _state.update { state -> state.copy(predictionDialogOpen = false) }
            MatchesEvent.OpenDialog -> _state.update { state -> state.copy(predictionDialogOpen = true) }
            is MatchesEvent.SelectMatch -> _state.update { state -> state.copy(selectedMatch = event.match) }
            is MatchesEvent.SubmitPrediction -> submitPrediction()
            MatchesEvent.UpdatePrediction -> updatePrediction()
        }
    }

    private suspend fun getPredictions() {
        val predictions = api.getPredictions()
        val predictionsBody = predictions.body()

        if (predictions.isSuccessful && predictionsBody != null) {
            _state.update { state ->
                state.copy(
                    predictions = predictionsBody,
                )
            }
        }
    }

    private suspend fun getMatches() {
        val matches = api.getMatches(2025)
        val matchesBody = matches.body()
        if (matches.isSuccessful && matchesBody != null) {
            _state.update { state ->
                state.copy(
                    matches = matchesBody,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getScores() {
        val scores = api.getUserScores()
        val scoresBody = scores.body()
        if (scores.isSuccessful && scoresBody != null) {
            _state.update { state ->
                state.copy(
                    scores = scoresBody,
                )
            }
        }
    }

    private fun submitPrediction() {
        viewModelScope.launch {
            api.addPrediction(
                AddOrEditPredictionRequest(
                    _state.value.selectedMatch.id,
                    _state.value.homePickerState.selectedItem.toInt(),
                    _state.value.awayPickerState.selectedItem.toInt()
                )
            )
            getPredictions()
        }
    }

    private fun updatePrediction() {
        viewModelScope.launch {
            api.updatePrediction(
                AddOrEditPredictionRequest(
                    _state.value.selectedMatch.id,
                    _state.value.homePickerState.selectedItem.toInt(),
                    _state.value.awayPickerState.selectedItem.toInt()
                )
            )
            getPredictions()
        }
    }
}

data class MatchesState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val homePickerState: PickerState = PickerState(),
    val awayPickerState: PickerState = PickerState(),
    val matches: List<Match> = emptyList(),
    val scores: List<Score> = emptyList(),
    val predictions: List<Prediction> = emptyList(),
    val predictionDialogOpen: Boolean = false,
    val selectedMatch: Match = Match(0, "", "", 0, 0, 0, "", 0, 0)
)

sealed interface MatchesEvent {
    data object OpenDialog : MatchesEvent
    data object CloseDialog : MatchesEvent
    data class SelectMatch(val match: Match) : MatchesEvent
    data object SubmitPrediction : MatchesEvent
    data object UpdatePrediction : MatchesEvent
}