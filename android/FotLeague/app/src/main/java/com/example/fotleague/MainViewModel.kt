package com.example.fotleague

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(api: FotLeagueApi, authStatus: AuthStatus) :
    ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val userResponse = api.getAuthUser()
                if (userResponse.code() == 200) {
                    _state.update { state -> state.copy(isLoggedIn = true, isLoading = false) }
                    authStatus.setAuthUser(userResponse.body())
                } else {
                    _state.update { state -> state.copy(isLoggedIn = false, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Connection failed") }
            }
        }
    }
}

data class MainState(
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true
)