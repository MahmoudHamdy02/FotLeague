package com.example.fotleague

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext context: Context, api: FotLeagueApi) :
    ViewModel() {
    private val dataStoreUtil = DataStoreUtil(context)

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreUtil.getAuthCookie.collect {
                Log.d("COOKIE", it)
                if (it.isEmpty()) {
                    _state.update { state -> state.copy(isLoggedIn = false, isLoading = false) }
                } else {
                    val userResponse = api.getAuthUser(it)
                    Log.d("RESPONSE", userResponse.toString())
                    Log.d("RESPONSE", userResponse.body().toString())
                    if (userResponse.code() == 200) {
                        _state.update { state -> state.copy(isLoggedIn = true, isLoading = false) }
                    } else {
                        _state.update { state -> state.copy(isLoggedIn = false, isLoading = false) }
                    }
                }
            }
        }
    }
}

data class MainState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true
)