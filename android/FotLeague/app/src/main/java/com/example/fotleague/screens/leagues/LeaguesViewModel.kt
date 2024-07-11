package com.example.fotleague.screens.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.League
import com.example.fotleague.models.network.request.CreateLeagueRequest
import com.example.fotleague.models.network.request.JoinLeagueRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(private val api: FotLeagueApi, authStatus: AuthStatus) :
    ViewModel() {

    private val _state = MutableStateFlow(LeaguesState())
    val state: StateFlow<LeaguesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authStatus.getAuthUser().collect {
                if (it != null) {
                    getLeagues()
                }
            }
        }
    }

    private suspend fun getLeagues() {
        val leaguesResponse = api.getLeagues()
        val leaguesBody = leaguesResponse.body()
        if (leaguesResponse.isSuccessful && leaguesBody != null) {
            _state.update { state -> state.copy(leagues = leaguesBody) }
        }
    }

    fun onEvent(event: LeaguesEvent) {
        when (event) {
            LeaguesEvent.CloseJoinLeagueDialog -> _state.update { state -> state.copy(isJoinLeagueDialogOpen = false) }
            LeaguesEvent.OpenJoinLeagueDialog -> _state.update { state -> state.copy(isJoinLeagueDialogOpen = true) }
            is LeaguesEvent.SetJoinLeagueDialogCode -> _state.update { state -> state.copy(joinLeagueDialogCode = event.code) }
            LeaguesEvent.JoinLeague -> {
                viewModelScope.launch {
                    joinLeague()
                    _state.update { state -> state.copy(isJoinLeagueDialogOpen = false, joinLeagueDialogCode = "") }
                }
            }

            LeaguesEvent.CloseCreateLeagueDialog -> _state.update { state -> state.copy(isCreateLeagueDialogOpen = false) }
            LeaguesEvent.OpenCreateLeagueDialog -> _state.update { state -> state.copy(isCreateLeagueDialogOpen = true) }
            is LeaguesEvent.SetCreateLeagueDialogName -> _state.update { state -> state.copy(createLeagueDialogName = event.name) }
            LeaguesEvent.CreateLeague -> {
                viewModelScope.launch {
                    createLeague()
                    _state.update { state -> state.copy(isCreateLeagueDialogOpen = false, createLeagueDialogName = "") }
                }
            }
        }
    }

    private suspend fun joinLeague() {
        val response = api.joinLeague(JoinLeagueRequest(_state.value.joinLeagueDialogCode))
        if (response.isSuccessful) {
            getLeagues()
        }
    }

    private suspend fun createLeague() {
        val response = api.createLeague(CreateLeagueRequest(_state.value.createLeagueDialogName))
        if (response.isSuccessful) {
            getLeagues()
        }
    }
}

data class LeaguesState(
    val leagues: List<League> = emptyList(),

    val isJoinLeagueDialogOpen: Boolean = false,
    val joinLeagueDialogCode: String = "",

    val isCreateLeagueDialogOpen: Boolean = false,
    val createLeagueDialogName: String = ""
)

sealed interface LeaguesEvent {
    data object OpenJoinLeagueDialog: LeaguesEvent
    data object CloseJoinLeagueDialog: LeaguesEvent
    data class SetJoinLeagueDialogCode(val code: String): LeaguesEvent
    data object JoinLeague: LeaguesEvent

    data object OpenCreateLeagueDialog: LeaguesEvent
    data object CloseCreateLeagueDialog: LeaguesEvent
    data class SetCreateLeagueDialogName(val name: String): LeaguesEvent
    data object CreateLeague: LeaguesEvent
}