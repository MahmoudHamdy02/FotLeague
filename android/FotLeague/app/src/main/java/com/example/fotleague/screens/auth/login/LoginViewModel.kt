package com.example.fotleague.screens.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.LifecycleUtil
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.LoginRequest
import com.example.fotleague.models.network.request.SignUpRequest
import com.example.fotleague.screens.matches.MatchesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: FotLeagueApi
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> login()
            is LoginEvent.SetEmail -> _state.update { state -> state.copy(email = event.email) }
            is LoginEvent.SetPassword -> _state.update { state -> state.copy(password = event.password) }
            is LoginEvent.SetRememberMe -> _state.update { state -> state.copy(rememberMe = event.rememberMe) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val response = api.login(LoginRequest(_state.value.email, _state.value.password, _state.value.rememberMe))
            Log.d("LOGIN", response.body().toString())
            LifecycleUtil.onSetRestartTrue()
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false
)

sealed interface LoginEvent {
    data class SetEmail(val email: String): LoginEvent
    data class SetPassword(val password: String): LoginEvent
    data class SetRememberMe(val rememberMe: Boolean): LoginEvent
    data object Login : LoginEvent
}