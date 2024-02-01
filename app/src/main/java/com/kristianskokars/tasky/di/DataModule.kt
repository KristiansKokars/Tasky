package com.kristianskokars.tasky.di

import android.app.AlarmManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.kristianskokars.tasky.BuildConfig
import com.kristianskokars.tasky.core.data.local.AndroidScheduler
import com.kristianskokars.tasky.core.data.local.db.EventDao
import com.kristianskokars.tasky.core.data.local.db.ReminderDao
import com.kristianskokars.tasky.core.data.local.db.TaskDao
import com.kristianskokars.tasky.core.data.local.db.TaskyDatabase
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.core.data.local.userSettingsStore
import com.kristianskokars.tasky.core.data.remote.NetworkClient
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.feature.event.presentation.AndroidPhotoConverter
import com.kristianskokars.tasky.feature.event.presentation.PhotoConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
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
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager = context.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun provideScheduler(@ApplicationContext context: Context, alarmManager: AlarmManager): Scheduler = AndroidScheduler(context, alarmManager)

    @Provides
    @Singleton
    fun provideTaskyDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(
            context,
            TaskyDatabase::class.java,
            "tasky-database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskyDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideEventDao(database: TaskyDatabase): EventDao = database.eventDao()

    @Provides
    @Singleton
    fun provideReminderDao(database: TaskyDatabase): ReminderDao = database.reminderDao()

    @Provides
    @Singleton
    fun providePhotoConverter(@ApplicationContext context: Context): PhotoConverter =
        AndroidPhotoConverter(context, ioDispatcher = Dispatchers.IO)

    @Provides
    @Singleton
    fun provideUserSettingsStore(
        @ApplicationContext context: Context
    ): DataStore<UserSettings> = context.userSettingsStore

    @Provides
    @Singleton
    fun provideRetrofit(userSettingsStore: DataStore<UserSettings>) =
        NetworkClient.createRetrofitClient(
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
