package com.example.fotleague.di

import android.util.Log
import com.example.fotleague.data.FotLeagueApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.net.InetAddress
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFotLeagueApi(): FotLeagueApi? {
        var address: String?
        runBlocking {
            address = getAddress()
        }
        Log.d("NET", address ?: "No address found")
        if (address == null) {
            return null
        }
        return Retrofit.Builder()
            .baseUrl("http://$address:3001")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FotLeagueApi::class.java)
    }

    // Temporary until backend is hosted
    private suspend fun getAddress(): String? {
        try {
            val address = withContext(Dispatchers.IO) {
                InetAddress.getByName("mahmoud-PC.local")
            }
            Log.d("NET", address.hostAddress ?: "No address")
            return address.hostAddress
        } catch (e: Exception) {
            return null
        }
    }
}
