package com.kristianskokars.tasky.core.data.remote

import com.kristianskokars.tasky.core.data.remote.model.LoginDTO
import com.kristianskokars.tasky.core.data.remote.model.LoginResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.RegisterDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskyAPI {
    @POST("register")
    suspend fun register(@Body body: RegisterDTO)

    @POST("login")
    suspend fun login(@Body body: LoginDTO): LoginResponseDTO
}
