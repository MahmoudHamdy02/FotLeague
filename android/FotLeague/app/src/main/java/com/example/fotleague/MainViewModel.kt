package com.example.fotleague

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val api: FotLeagueApi, val authStatus: AuthStatus) :
    ViewModel() {

    fun init() {
        viewModelScope.launch {
            try {
                val userResponse = api.getAuthUser()
                val body = userResponse.body()
                if (userResponse.code() == 200 && body != null) {
                    authStatus.setAuthState(
                        AuthState(
                            user = userResponse.body(),
                            isLoading = false,
                            isLoggedIn = true
                        )
                    )
                } else {
                    authStatus.setAuthState(AuthState(isLoading = false, isLoggedIn = false))
                }
            } catch (e: Exception) {
                authStatus.setAuthState(AuthState(error = "Connection failed", isLoading = false))
            }
        }
    }
}
