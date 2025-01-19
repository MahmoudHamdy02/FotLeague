package com.example.fotleague.screens.leagues.leaguesettings

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.LeaveLeagueRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueSettingsViewModel @Inject constructor(
    private val api: FotLeagueApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val leagueId = savedStateHandle.get<Int>("leagueId")
    private val isLeagueOwner = savedStateHandle.get<Boolean>("isLeagueOwner")

    private val _state = MutableStateFlow(LeagueSettingsState(isLeagueOwner == true))
    val state: StateFlow<LeagueSettingsState> = _state.asStateFlow()

    fun onEvent(event: LeagueSettingsEvent) {
        when (event) {
            LeagueSettingsEvent.LeaveLeague -> {
                viewModelScope.launch { leaveLeague() }
            }
            LeagueSettingsEvent.DeleteLeague -> {
                viewModelScope.launch { deleteLeague() }
            }
            LeagueSettingsEvent.CloseLeaveLeagueDialog -> _state.update { it.copy(isLeaveLeagueDialogOpen = false) }
            LeagueSettingsEvent.OpenLeaveLeagueDialog -> _state.update { it.copy(isLeaveLeagueDialogOpen = true) }
            LeagueSettingsEvent.CloseDeleteLeagueDialog -> _state.update { it.copy(isDeleteLeagueDialogOpen = false) }
            LeagueSettingsEvent.OpenDeleteLeagueDialog -> _state.update { it.copy(isDeleteLeagueDialogOpen = true) }
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

    private suspend fun deleteLeague() {
        if (leagueId != null) {
            val leagueDetailsResponse = api.deleteLeague(leagueId)
            Log.d("DELETE", leagueDetailsResponse.toString())
            if (leagueDetailsResponse.isSuccessful) {
                _state.update { state ->
                    state.copy(leagueLeft = true)
                }
            }
        }
    }
}

data class LeagueSettingsState(
    val isLeagueOwner: Boolean,
    val leagueLeft: Boolean = false,
    val isLeaveLeagueDialogOpen: Boolean = false,
    val isDeleteLeagueDialogOpen: Boolean = false
)

sealed interface LeagueSettingsEvent {
    data object LeaveLeague : LeagueSettingsEvent
    data object DeleteLeague : LeagueSettingsEvent
    data object OpenLeaveLeagueDialog : LeagueSettingsEvent
    data object CloseLeaveLeagueDialog : LeagueSettingsEvent
    data object OpenDeleteLeagueDialog : LeagueSettingsEvent
    data object CloseDeleteLeagueDialog : LeagueSettingsEvent
}