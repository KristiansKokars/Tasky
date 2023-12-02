package com.kristianskokars.tasky.core.data.remote

import androidx.datastore.core.DataStore
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userSettingsStore: DataStore<UserSettings>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return runBlocking {
            val token = userSettingsStore.data.first().token
                ?: return@runBlocking chain.proceed(request)

            val newRequest = request.newBuilder()
                .headers(request.headers)
                .header("Authorization", "Bearer $token")
                .method(request.method, request.body)
                .build()
            chain.proceed(newRequest)
        }
    }
}
