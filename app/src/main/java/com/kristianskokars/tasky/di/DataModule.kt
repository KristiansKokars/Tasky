package com.kristianskokars.tasky.di

import com.kristianskokars.tasky.BuildConfig
import com.kristianskokars.tasky.core.data.remote.NetworkClient
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRetrofit() = NetworkClient.createRetrofitClient(
        BuildConfig.apiLink,
        BuildConfig.apiKey
    )

    @Provides
    @Singleton
    fun provideTaskyAPI(retrofit: Retrofit): TaskyAPI = retrofit.create(TaskyAPI::class.java)
}
