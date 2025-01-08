package com.example.fotleague.screens.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.AuthState
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    val api: FotLeagueApi,
    val authStatus: AuthStatus
) : ViewModel() {
    private val _state = MutableStateFlow(MoreState())
    val state: StateFlow<MoreState> = _state.asStateFlow()

    val authState = authStatus.getAuthState()

    fun onEvent(event: MoreEvent) {
        when (event) {
            MoreEvent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            api.logout()
            authStatus.setAuthState(AuthState(isLoading = false, isLoggedIn = false))
            authStatus.logoutTrigger.value = true
            authStatus.logoutTrigger.value = false
            _state.update { state -> state.copy(onLogout = true) }
        }
    }

}

data class MoreState(
    val onLogout: Boolean = false
)

sealed interface MoreEvent {
    data object Logout : MoreEvent
}