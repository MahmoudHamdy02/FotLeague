package com.example.fotleague.di

import android.content.Context
import com.example.fotleague.AuthStatus
import com.example.fotleague.R
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

    @Provides
    @Singleton
    fun provideFotLeagueApi(
        dataStoreUtil: DataStoreUtil,
        @ApplicationContext context: Context
    ): FotLeagueApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(AddCookiesInterceptor(dataStoreUtil))
                    .addInterceptor(ReceivedCookiesInterceptor(dataStoreUtil))
                    .build()
            )
            .baseUrl(context.getString(R.string.backend))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FotLeagueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context)
    }

    @Provides
    @Singleton
    fun provideAuthStatus(api: FotLeagueApi): AuthStatus {
        return AuthStatus(api)
    }
}
