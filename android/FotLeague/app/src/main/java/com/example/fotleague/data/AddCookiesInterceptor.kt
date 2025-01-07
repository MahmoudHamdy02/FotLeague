package com.example.fotleague.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor(private val dataStoreUtil: DataStoreUtil) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val cookie = dataStoreUtil.getAuthCookie.value
        builder.addHeader("Cookie", cookie)

        return chain.proceed(builder.build())
    }
}