package com.kristianskokars.tasky.feature.auth.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.RegisterDTO
import com.kristianskokars.tasky.feature.auth.data.model.RegisterError
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.success
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

@Singleton
class BackendAuthProvider @Inject constructor(private val api: TaskyAPI) {
    suspend fun register(name: String, email: String, password: String): Result<Success, RegisterError> {
        try {
            api.register(
                RegisterDTO(
                    fullName = name,
                    email = email,
                    password = password
                )
            )
        } catch (exception: HttpException) {
            return Err(RegisterError.ServerError)
        } catch (exception: UnknownHostException) {
            return Err(RegisterError.NetworkError)
        }

        return success()
    }
}
