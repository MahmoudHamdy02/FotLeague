package com.example.fotleague.data

import android.util.Log
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor(private val dataStoreUtil: DataStoreUtil) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        Log.d("TEST", "intercept: 1")
        val cookie = dataStoreUtil.getAuthCookie.value
        Log.d("INTERCEPTOR COOKIE", cookie)

        builder.addHeader("Cookie", cookie)

        return chain.proceed(builder.build())
    }
}