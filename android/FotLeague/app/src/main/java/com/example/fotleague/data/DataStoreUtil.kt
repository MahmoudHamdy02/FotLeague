package com.example.fotleague.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class DataStoreUtil(@ApplicationContext private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val AUTH_COOKIE = stringPreferencesKey("auth_cookie")
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    val getAuthCookie: StateFlow<String> = runBlocking {
        context.dataStore.data
            .map { preferences ->
                preferences[AUTH_COOKIE] ?: ""
            }.stateIn(scope)
    }

    suspend fun setAuthCookie(value: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_COOKIE] = value
        }
    }
}