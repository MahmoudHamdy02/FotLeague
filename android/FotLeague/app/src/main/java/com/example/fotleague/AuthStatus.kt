package com.example.fotleague

import android.util.Log
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

class AuthStatus @Inject constructor(val api: FotLeagueApi) {

    val loginTrigger = MutableStateFlow(false)
    val logoutTrigger = MutableStateFlow(false)
    private var authState = MutableStateFlow(AuthState())

    fun setAuthState(state: AuthState) {
        Log.d("STATE", "$state")
        authState.value = state
    }

    fun getAuthState(): MutableStateFlow<AuthState> {
        return authState
    }
}