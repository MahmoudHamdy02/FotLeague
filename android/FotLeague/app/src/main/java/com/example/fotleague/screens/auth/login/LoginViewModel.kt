package com.example.fotleague.screens.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.LifecycleUtil
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.LoginRequest
import com.example.fotleague.models.network.request.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: FotLeagueApi,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            val response = api.login(LoginRequest("fotleague@fotleague.com", "fotleague", false))
            Log.d("LOGIN", response.body().toString())
            LifecycleUtil.onSetRestartTrue()
        }
    }
}

sealed interface LoginEvent {
    data object Login : LoginEvent
}