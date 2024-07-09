package com.example.fotleague.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class DataStoreUtil(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val AUTH_COOKIE = stringPreferencesKey("auth_cookie")
    }

    val getAuthCookie: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[AUTH_COOKIE] ?: ""
        }

    suspend fun getAuthCookie(): String {
        return getAuthCookie.last()
    }

    suspend fun setAuthCookie(value: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_COOKIE] = value
        }
    }
}