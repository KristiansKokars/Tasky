package com.kristianskokars.tasky.core.data.remote

import com.kristianskokars.tasky.core.data.remote.model.AgendaResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.CreateReminderRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.CreateTaskRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.EventRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.GetAttendeeResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.RegisterRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.ReminderResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.TaskResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateReminderRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateTaskRequestDTO
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("event")
    @Multipart
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: EventRequestDTO,
        @Part photos: List<MultipartBody.Part>
    )

    @DELETE("event")
    suspend fun deleteEvent(
        @Query("eventId") eventId: String
    )

    @GET("attendee")
    suspend fun getAttendee(@Query("email") email: String): GetAttendeeResponseDTO

    @POST("task")
    suspend fun createTask(
        @Body body: CreateTaskRequestDTO
    )

    @GET("task")
    suspend fun getTask(
        @Query("taskId") taskId: String
    ): TaskResponseDTO

    @DELETE("task")
    suspend fun deleteTask(
        @Query("taskId") taskId: String
    )

    @PUT("task")
    suspend fun updateTask(
        @Body body: UpdateTaskRequestDTO
    )

    @POST("reminder")
    suspend fun createReminder(
        @Body body: CreateReminderRequestDTO
    )

    @GET("reminder")
    suspend fun getReminder(
        @Query("reminderId") reminderId: String
    ): ReminderResponseDTO

    @DELETE("reminder")
    suspend fun deleteReminder(
        @Query("reminderId") reminderId: String
    )

    @PUT("reminder")
    suspend fun updateReminder(
        @Body body: UpdateReminderRequestDTO
    )
}
