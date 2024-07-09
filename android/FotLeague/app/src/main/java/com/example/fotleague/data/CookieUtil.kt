package com.example.fotleague.data

import kotlinx.coroutines.flow.Flow
import java.net.CookieManager
import java.net.HttpCookie

object CookieUtil {
    val cookieManager = CookieManager()

    fun addCookie(cookie: HttpCookie) {
        cookieManager.cookieStore.add(null, cookie)
    }

    fun getCookies(): List<HttpCookie> {
        return cookieManager.cookieStore.cookies
    }
}