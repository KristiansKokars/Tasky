package com.kristianskokars.tasky.core.data.remote

import com.kristianskokars.tasky.core.data.remote.model.AgendaResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.RegisterRequestDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskyAPI {
    @POST("register")
    suspend fun register(@Body body: RegisterRequestDTO)

    @POST("login")
    suspend fun login(@Body body: LoginRequestDTO): LoginResponseDTO

    @GET("agenda")
    suspend fun agendaForTheDay(
        @Query("timezone") timezone: String,
        @Query("time") time: Long
    ): AgendaResponseDTO
}
