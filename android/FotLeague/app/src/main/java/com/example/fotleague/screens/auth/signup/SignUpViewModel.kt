package com.example.fotleague.screens.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.network.request.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val api: FotLeagueApi,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.SignUp -> signUp()
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            val response = api.signUp(SignUpRequest("test14@test.com", "fnoeuwf23", "mahmoud14"))
            Log.d("SIGNUP", response.body().toString())
            Log.d("SIGNUP", response.headers()["Set-Cookie"] ?: "No cookie found")
            dataStoreUtil.setAuthCookie(response.headers()["Set-Cookie"] ?: "")
        }
    }
}

sealed interface SignUpEvent {
    data object SignUp : SignUpEvent
}