package com.kristianskokars.tasky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @IOScope
    @Singleton
    @Provides
    fun provideIOScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOScope
