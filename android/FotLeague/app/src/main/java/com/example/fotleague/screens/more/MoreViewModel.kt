package com.example.fotleague.screens.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotleague.LifecycleUtil
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    val api: FotLeagueApi
) : ViewModel() {

    fun onEvent(event: MoreEvent) {
        when(event) {
            MoreEvent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            api.logout()
            LifecycleUtil.onSetRestartTrue()
        }
    }

}

sealed interface MoreEvent {
    data object Logout : MoreEvent
}