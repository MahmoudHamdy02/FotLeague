package com.example.fotleague.data

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


class ReceivedCookiesInterceptor @Inject constructor(private val dataStoreUtil: DataStoreUtil) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            runBlocking {
                dataStoreUtil.setAuthCookie(originalResponse.headers("Set-Cookie")[0])
            }
        }

        return originalResponse
    }
}