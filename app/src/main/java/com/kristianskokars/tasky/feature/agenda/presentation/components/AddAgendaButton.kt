package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.data.local.model.CreateAgendaType
import com.kristianskokars.tasky.core.presentation.components.TaskyDropdownMenu
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White

private val addAgendaDropdownItems
    @Composable
    get() = listOf(
        stringResource(id = R.string.event),
        stringResource(id = R.string.task),
        stringResource(id = R.string.reminder)
    )

private fun agendaTypeFromIndex(index: Int): CreateAgendaType = when (index) {
    0 -> CreateAgendaType.Event
    1 -> CreateAgendaType.Task
    2 -> CreateAgendaType.Reminder
    else -> throw IllegalArgumentException("Index out of range for agenda types")
}

@Composable
fun AddAgendaButton(
    onCreateNewAgenda: (type: CreateAgendaType) -> Unit,
) {
    var isDropdownOpen by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomStart) {
        FloatingActionButton(
            containerColor = Black,
            contentColor = White,
            onClick = { isDropdownOpen = true }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = stringResource(
                    R.string.add_new_agenda_item
                )
            )
        }
        TaskyDropdownMenu(
            items = addAgendaDropdownItems,
            onItemClick = {
                isDropdownOpen = false
                onCreateNewAgenda(agendaTypeFromIndex(it))
            },
            isExpanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false }
        )
    }
}
