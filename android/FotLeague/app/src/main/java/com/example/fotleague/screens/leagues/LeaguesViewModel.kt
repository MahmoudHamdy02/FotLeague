package com.example.fotleague.screens.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.League
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

    }
}

data class LeaguesState(
    val leagues: List<League> = emptyList()
)

sealed interface LeaguesEvent {

}