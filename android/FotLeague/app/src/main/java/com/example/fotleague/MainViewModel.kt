package com.example.fotleague

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val api: FotLeagueApi, val authStatus: AuthStatus) :
    ViewModel() {

//    private val _state = MutableStateFlow(MainState())
//    val state: StateFlow<MainState> = _state.asStateFlow()

    fun init() {
        Log.d("AUTH", "{userResponse.code()}")
        viewModelScope.launch {
            try {
                val userResponse = api.getAuthUser()
                val body = userResponse.body()
                if (userResponse.code() == 200 && body != null) {
//                    _state.update { state -> state.copy(isLoggedIn = true, isLoading = false, user = body) }
                    authStatus.setAuthState(
                        AuthState(
                            user = userResponse.body(),
                            isLoading = false,
                            isLoggedIn = true
                        )
                    )
                } else {
//                    _state.update { state -> state.copy(isLoggedIn = false, isLoading = false) }
                    authStatus.setAuthState(AuthState(isLoading = false, isLoggedIn = false))
                }
            } catch (e: Exception) {
//                _state.update { it.copy(error = "Connection failed") }
                authStatus.setAuthState(AuthState(error = "Connection failed", isLoading = false))
            }
        }
    }
}

//data class MainState(
//    val error: String? = null,
//    val isLoggedIn: Boolean = false,
//    val isLoading: Boolean = true,
//    val user: User = User(0, "", "", 2)
//)