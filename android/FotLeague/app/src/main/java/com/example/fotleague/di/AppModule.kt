package com.example.fotleague.di

import android.content.Context
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFotLeagueApi(): FotLeagueApi {
//        var address: String?
//        runBlocking {
//            address = getAddress()
//        }
//        Log.d("NET", address ?: "No address found")
//        if (address == null) {
//            return null
//        }
        return Retrofit.Builder()
            .baseUrl("http://mahmoud-PC.local:3001")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FotLeagueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context)
    }

    // Temporary until backend is hosted
//    private suspend fun getAddress(): String? {
//        try {
//            val address = withContext(Dispatchers.IO) {
//                InetAddress.getByName("mahmoud-PC.local")
//            }
//            Log.d("NET", address.hostAddress ?: "No address")
//            return address.hostAddress
//        } catch (e: Exception) {
//            return null
//        }
//    }
}
