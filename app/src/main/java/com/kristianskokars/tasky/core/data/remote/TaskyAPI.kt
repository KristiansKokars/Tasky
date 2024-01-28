package com.kristianskokars.tasky.core.data.remote

import com.kristianskokars.tasky.core.data.remote.model.AgendaResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.CreateTaskRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.EventRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.GetAttendeeResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.RegisterRequestDTO
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("task")
    suspend fun createTask(
        @Body body: CreateTaskRequestDTO
    )

    @POST("event")
    @Multipart
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: EventRequestDTO,
        @Part photos: List<MultipartBody.Part>
    )

    @GET("attendee")
    suspend fun getAttendee(@Query("email") email: String): GetAttendeeResponseDTO

    @DELETE("task")
    suspend fun deleteTask(
        @Query("taskId") taskId: String
    )
}
