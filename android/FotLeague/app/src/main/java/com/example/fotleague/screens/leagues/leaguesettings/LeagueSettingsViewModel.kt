package com.example.fotleague.screens.leagues.leaguesettings

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.fotleague.Screen
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.GenerateNewLeagueCodeRequest
import com.example.fotleague.models.network.request.LeaveLeagueRequest
import com.example.fotleague.models.network.request.RenameLeagueRequest
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
    private val args = savedStateHandle.toRoute<Screen.LeagueSettings>()

    private val _state = MutableStateFlow(LeagueSettingsState(args.isLeagueOwner == true))
    val state: StateFlow<LeagueSettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getLeagueDetailsAndUpdateName()
        }
    }

    fun onEvent(event: LeagueSettingsEvent) {
        when (event) {
            LeagueSettingsEvent.OpenGenerateNewLeagueCodeDialog -> _state.update {
                it.copy(isGenerateNewLeagueCodeDialogOpen = true)
            }

            LeagueSettingsEvent.CloseGenerateNewLeagueCodeDialog -> _state.update {
                it.copy(isGenerateNewLeagueCodeDialogOpen = false)
            }

            LeagueSettingsEvent.OpenRenameLeagueDialog -> _state.update {
                it.copy(isRenameLeagueDialogOpen = true)
            }

            LeagueSettingsEvent.CloseRenameLeagueDialog -> _state.update {
                it.copy(isRenameLeagueDialogOpen = false)
            }

            is LeagueSettingsEvent.SetRenamedLeagueName -> _state.update {
                it.copy(renamedLeagueName = event.name)
            }

            LeagueSettingsEvent.RenameLeague -> {
                viewModelScope.launch { renameLeague() }
            }

            LeagueSettingsEvent.GenerateNewCode -> {
                viewModelScope.launch { generateNewCode() }
            }

            LeagueSettingsEvent.LeaveLeague -> {
                viewModelScope.launch { leaveLeague() }
            }

            LeagueSettingsEvent.DeleteLeague -> {
                viewModelScope.launch { deleteLeague() }
            }

            LeagueSettingsEvent.CloseLeaveLeagueDialog -> _state.update {
                it.copy(
                    isLeaveLeagueDialogOpen = false
                )
            }

            LeagueSettingsEvent.OpenLeaveLeagueDialog -> _state.update {
                it.copy(
                    isLeaveLeagueDialogOpen = true
                )
            }

            LeagueSettingsEvent.CloseDeleteLeagueDialog -> _state.update {
                it.copy(
                    isDeleteLeagueDialogOpen = false
                )
            }

            LeagueSettingsEvent.OpenDeleteLeagueDialog -> _state.update {
                it.copy(
                    isDeleteLeagueDialogOpen = true
                )
            }
        }
    }

    private suspend fun getLeagueDetailsAndUpdateName() {
        val leagueDetailsResponse = api.getLeagueDetails(args.leagueId)
        val leagueDetailsBody = leagueDetailsResponse.body()
        if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
            _state.update { state ->
                state.copy(renamedLeagueName = leagueDetailsBody.league.name)
            }
        }
    }

    private suspend fun renameLeague() {
        val leagueDetailsResponse =
            api.renameLeague(RenameLeagueRequest(args.leagueId, _state.value.renamedLeagueName))
        val leagueDetailsBody = leagueDetailsResponse.body()
        if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
            _state.update { state ->
                state.copy(leagueLeft = true)
            }
        }
    }

    private suspend fun generateNewCode() {
        val leagueDetailsResponse = api.generateNewLeagueCode(GenerateNewLeagueCodeRequest(args.leagueId))
        val leagueDetailsBody = leagueDetailsResponse.body()
        Log.d("GENERATE", leagueDetailsBody.toString())
        if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
            _state.update { state ->
                state.copy(leagueLeft = true)
            }
        }
    }

    private suspend fun leaveLeague() {
        val leagueDetailsResponse = api.leaveLeague(LeaveLeagueRequest(args.leagueId))
        val leagueDetailsBody = leagueDetailsResponse.body()
        if (leagueDetailsResponse.isSuccessful && leagueDetailsBody != null) {
            _state.update { state ->
                state.copy(leagueLeft = true)
            }
        }
    }

    private suspend fun deleteLeague() {
        val leagueDetailsResponse = api.deleteLeague(args.leagueId)
        Log.d("DELETE", leagueDetailsResponse.toString())
        if (leagueDetailsResponse.isSuccessful) {
            _state.update { state ->
                state.copy(leagueLeft = true)
            }
        }
    }
}

data class LeagueSettingsState(
    val isLeagueOwner: Boolean,
    val renamedLeagueName: String = "",
    val isRenameLeagueDialogOpen: Boolean = false,
    val leagueLeft: Boolean = false,
    val isLeaveLeagueDialogOpen: Boolean = false,
    val isDeleteLeagueDialogOpen: Boolean = false,
    val isGenerateNewLeagueCodeDialogOpen: Boolean = false
)

sealed interface LeagueSettingsEvent {
    data object OpenRenameLeagueDialog : LeagueSettingsEvent
    data object CloseRenameLeagueDialog : LeagueSettingsEvent
    data class SetRenamedLeagueName(val name: String) : LeagueSettingsEvent
    data object RenameLeague : LeagueSettingsEvent
    data object LeaveLeague : LeagueSettingsEvent
    data object DeleteLeague : LeagueSettingsEvent
    data object GenerateNewCode: LeagueSettingsEvent
    data object OpenLeaveLeagueDialog : LeagueSettingsEvent
    data object CloseLeaveLeagueDialog : LeagueSettingsEvent
    data object OpenDeleteLeagueDialog : LeagueSettingsEvent
    data object CloseDeleteLeagueDialog : LeagueSettingsEvent
    data object OpenGenerateNewLeagueCodeDialog : LeagueSettingsEvent
    data object CloseGenerateNewLeagueCodeDialog : LeagueSettingsEvent
}