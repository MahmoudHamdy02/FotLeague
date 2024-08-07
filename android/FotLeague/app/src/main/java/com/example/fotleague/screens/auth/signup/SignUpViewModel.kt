package com.example.fotleague.screens.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.LifecycleUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val api: FotLeagueApi
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.SignUp -> signUp()
            is SignUpEvent.SetEmail -> _state.update { state -> state.copy(email = event.email) }
            is SignUpEvent.SetPassword -> _state.update { state -> state.copy(password = event.password) }
            is SignUpEvent.SetUsername -> _state.update { state -> state.copy(username = event.username) }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            val response = api.signUp(SignUpRequest(_state.value.email, _state.value.password, _state.value.username))
            Log.d("SIGNUP", response.body().toString())
            Log.d("SIGNUP", response.headers()["Set-Cookie"] ?: "No cookie found")
            LifecycleUtil.onSetRestartTrue()
        }
    }
}

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val username: String = ""
)

sealed interface SignUpEvent {
    data class SetUsername(val username: String): SignUpEvent
    data class SetEmail(val email: String): SignUpEvent
    data class SetPassword(val password: String): SignUpEvent
    data object SignUp : SignUpEvent
}