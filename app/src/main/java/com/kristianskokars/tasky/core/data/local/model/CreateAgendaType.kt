package com.kristianskokars.tasky.core.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface CreateAgendaType : Parcelable {
    data object Event : CreateAgendaType
    data object Reminder : CreateAgendaType
    data object Task : CreateAgendaType
}
