package com.kristianskokars.tasky.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.kristianskokars.tasky.BuildConfig
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.core.data.local.userSettingsStore
import com.kristianskokars.tasky.core.data.remote.NetworkClient
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import retrofit2.Retrofit
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideUserSettingsStore(
        @ApplicationContext context: Context
    ): DataStore<UserSettings> = context.userSettingsStore

    @Provides
    @Singleton
    fun provideRetrofit(userSettingsStore: DataStore<UserSettings>) = NetworkClient.createRetrofitClient(
        BuildConfig.apiLink,
        BuildConfig.apiKey,
        userSettingsStore
    )

    @Provides
    @Singleton
    fun provideTaskyAPI(retrofit: Retrofit): TaskyAPI = retrofit.create(TaskyAPI::class.java)

    @Provides
    @Singleton
    fun provideSystemClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun provideSystemTimezone(): TimeZone = TimeZone.currentSystemDefault()

    @Provides
    @Singleton
    fun provideSystemLocale(): Locale = Locale.getDefault()
}
