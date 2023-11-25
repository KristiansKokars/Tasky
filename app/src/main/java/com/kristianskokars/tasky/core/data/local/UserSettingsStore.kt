package com.kristianskokars.tasky.core.data.local

import android.content.Context
import androidx.datastore.dataStore

val Context.userSettingsStore by dataStore(
    fileName = "user-settings.json",
    serializer = UserSettingsSerializer(CryptoManager())
)
