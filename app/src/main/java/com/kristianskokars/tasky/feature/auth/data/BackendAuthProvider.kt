package com.kristianskokars.tasky.feature.auth.data

import androidx.datastore.core.DataStore
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.LoginRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.RegisterRequestDTO
import com.kristianskokars.tasky.core.domain.model.User
import com.kristianskokars.tasky.di.IOScope
import com.kristianskokars.tasky.feature.auth.domain.model.AuthState
import com.kristianskokars.tasky.feature.auth.domain.model.LoginError
import com.kristianskokars.tasky.feature.auth.domain.model.RegisterError
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.asStateFlow
import com.kristianskokars.tasky.lib.success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendAuthProvider @Inject constructor(
    private val api: TaskyAPI,
    private val userSettingsStore: DataStore<UserSettings>,
    @IOScope ioScope: CoroutineScope
) {
    val authState = userSettingsStore.data.map { userSettings ->
        if (userSettings.token != null && userSettings.userId != null && userSettings.fullName != null) {
            return@map AuthState.LoggedIn(
                User(userId = userSettings.userId, fullName = userSettings.fullName)
            )
        }

        return@map AuthState.LoggedOut
    }.asStateFlow(ioScope, AuthState.RetrievingFromStorage)

    suspend fun register(name: String, email: String, password: String): Result<Success, RegisterError> {
        try {
            api.register(
                RegisterRequestDTO(
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

    suspend fun login(email: String, password: String): Result<Success, LoginError> {
        try {
            val response = api.login(LoginRequestDTO(email, password))
            userSettingsStore.updateData { userSettings ->
                userSettings.copy(
                    token = response.token,
                    userId = response.userId,
                    fullName = response.fullName,
                    email = email
                )
            }
        } catch (exception: HttpException) {
            return Err(LoginError.ServerError)
        } catch (exception: UnknownHostException) {
            return Err(LoginError.NetworkError)
        } catch (exception: IOException) {
            return Err(LoginError.WriteToLocalStorageError)
        }

        return success()
    }

    suspend fun logout() {
        userSettingsStore.updateData { userSettings ->
            userSettings.copy(userId = null, fullName = null, token = null, email = null)
        }
    }
}
