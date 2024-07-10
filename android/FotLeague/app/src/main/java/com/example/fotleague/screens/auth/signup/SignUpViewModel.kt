package com.example.fotleague.screens.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.LifecycleUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val api: FotLeagueApi
) : ViewModel() {

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.SignUp -> signUp()
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            val response = api.signUp(SignUpRequest("fotleague@fotleague.com", "fotleague", "fotleague"))
            Log.d("SIGNUP", response.body().toString())
            Log.d("SIGNUP", response.headers()["Set-Cookie"] ?: "No cookie found")
            LifecycleUtil.onSetRestartTrue()
        }
    }
}

sealed interface SignUpEvent {
    data object SignUp : SignUpEvent
}