package com.kristianskokars.tasky.core.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyHeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .headers(request.headers)
            .header("x-api-key", apiKey)
            .method(request.method, request.body)
            .build()
        return chain.proceed(newRequest)
    }
}
