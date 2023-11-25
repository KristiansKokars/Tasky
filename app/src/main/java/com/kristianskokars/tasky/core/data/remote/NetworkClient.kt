package com.kristianskokars.tasky.core.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kristianskokars.tasky.BuildConfig
import com.kristianskokars.tasky.lib.json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkClient {
    private fun createOkHttpClient(apiKey: String): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(APIKeyHeaderInterceptor(apiKey))

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    fun createRetrofitClient(baseUrl: String, apiKey: String): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient(apiKey))
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
