package com.example.fotleague.di

import android.content.Context
import com.example.fotleague.AuthStatus
import com.example.fotleague.data.AddCookiesInterceptor
import com.example.fotleague.data.DataStoreUtil
import com.example.fotleague.data.FotLeagueApi
import com.example.fotleague.data.ReceivedCookiesInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Todo: add another api and builder to automatically restart the app if token is expired and 401 error is received

    @Provides
    @Singleton
    fun provideFotLeagueApi(dataStoreUtil: DataStoreUtil): FotLeagueApi {
//        var address: String?
//        runBlocking {
//            address = getAddress()
//        }
//        Log.d("NET", address ?: "No address found")
//        if (address == null) {
//            return null
//        }
        return Retrofit.Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(AddCookiesInterceptor(dataStoreUtil))
                    .addInterceptor(ReceivedCookiesInterceptor(dataStoreUtil))
                    .build()
            )
            .baseUrl("http://192.168.1.101:3001")
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

    @Provides
    @Singleton
    fun provideAuthStatus(api: FotLeagueApi): AuthStatus {
        return AuthStatus(api)
    }
}
