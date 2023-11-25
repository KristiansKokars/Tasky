package com.kristianskokars.tasky.core.data.local

import androidx.datastore.core.Serializer
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.lib.json
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException

class UserSettingsSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = json.encodeToString(
                serializer = UserSettings.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
