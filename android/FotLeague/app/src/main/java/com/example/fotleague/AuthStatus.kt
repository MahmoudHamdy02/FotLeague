package com.example.fotleague

import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthStatus @Inject constructor(val api: FotLeagueApi) {

    private var authUser = MutableStateFlow<User?>(null)

    fun setAuthUser(user: User?) {
        authUser.value = user
    }

    fun getAuthUser(): MutableStateFlow<User?> {
        return authUser
    }
}