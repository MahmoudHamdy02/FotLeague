package com.example.fotleague.screens.leagues.leaguesettings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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


}

data class LeagueSettingsState(
    val isLeagueOwner: Boolean
)

sealed interface LeagueSettingsEvent {
}