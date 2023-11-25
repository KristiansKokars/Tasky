package com.kristianskokars.tasky.core.data.remote

import androidx.datastore.core.DataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kristianskokars.tasky.BuildConfig
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.lib.json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkClient {
    private fun createOkHttpClient(apiKey: String, userSettingsStore: DataStore<UserSettings>): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(APIKeyHeaderInterceptor(apiKey))
        builder.addInterceptor(AuthInterceptor(userSettingsStore))

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    fun createRetrofitClient(
        baseUrl: String,
        apiKey: String,
        userSettingsStore: DataStore<UserSettings>
    ): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient(apiKey, userSettingsStore))
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
