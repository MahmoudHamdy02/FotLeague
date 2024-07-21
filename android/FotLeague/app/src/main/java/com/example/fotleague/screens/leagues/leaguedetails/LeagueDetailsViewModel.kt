package com.example.fotleague.screens.leagues.leaguedetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.League
import com.example.fotleague.models.UserScore
import com.example.fotleague.models.network.request.LeaveLeagueRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueDetailsViewModel @Inject constructor(
    private val api: FotLeagueApi,
    authStatus: AuthStatus,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _state = MutableStateFlow(LeagueDetailsState())
    val state: StateFlow<LeagueDetailsState> = _state.asStateFlow()

    private val leagueId = savedStateHandle.get<Int>("leagueId")

    init {
        if (leagueId != null) {
            viewModelScope.launch {
                authStatus.getAuthUser().collect {
                    if (it != null) {
                        getLeague(leagueId)
                    }
                }
            }
        }
    }

    fun onEvent(event: LeagueDetailsEvent) {
        when (event) {
            LeagueDetailsEvent.LeaveLeague -> {
                viewModelScope.launch { leaveLeague() }
            }
            LeagueDetailsEvent.CloseLeaveLeagueDialog -> _state.update { it.copy(isLeaveLeagueDialogOpen = false) }
            LeagueDetailsEvent.OpenLeaveLeagueDialog -> _state.update { it.copy(isLeaveLeagueDialogOpen = true) }
        }
    }

    private suspend fun getLeague(leagueId: Int) {
        val leagueDetailsResponse = api.getLeagueDetails(leagueId)
        val leagueDetailsBody = leagueDetailsResponse.body()
        if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
            Log.d("SCORES", leagueDetailsBody.userScores.toString())
            _state.update { state ->
                state.copy(
                    league = leagueDetailsBody.league,
                    userScores = leagueDetailsBody.userScores
                )
            }
        }
    }

    private suspend fun leaveLeague() {
        if (leagueId != null) {
            val leagueDetailsResponse = api.leaveLeague(LeaveLeagueRequest(leagueId))
            val leagueDetailsBody = leagueDetailsResponse.body()
            if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
                _state.update { state ->
                    state.copy(leagueLeft = true)
                }
            }
        }
    }

}

data class LeagueDetailsState(
    val league: League = League(0, "", 0, ""),
    val userScores: List<UserScore> = emptyList(),
    val leagueLeft: Boolean = false,
    val isLeaveLeagueDialogOpen: Boolean = false
)

sealed interface LeagueDetailsEvent {
    data object LeaveLeague : LeagueDetailsEvent
    data object OpenLeaveLeagueDialog : LeagueDetailsEvent
    data object CloseLeaveLeagueDialog : LeagueDetailsEvent
}