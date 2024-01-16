package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.util.formatToDate
import com.kristianskokars.tasky.core.presentation.util.formatToHHMM
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime

sealed interface TimeState {
    data object At : TimeState
    data object From : TimeState
    data object To : TimeState
}

// TODO: polish alignments here
@Composable
fun TaskyTimeSection(
    modifier: Modifier = Modifier,
    time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    timeState: TimeState = TimeState.At,
    isEditing: Boolean = false,
    onTimePicked: (LocalTime) -> Unit = {},
    onDatePicked: (LocalDate) -> Unit = {},
) {
    val editTimeDialogState = rememberMaterialDialogState()
    val editDateDialogState = rememberMaterialDialogState()

    // TODO: style dialogs
    MaterialDialog(
        dialogState = editTimeDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        timepicker { time -> onTimePicked(time.toKotlinLocalTime()) }
    }
    MaterialDialog(
        dialogState = editDateDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        datepicker { date -> onDatePicked(date.toKotlinLocalDate()) }
    }

    Column {
        Row(
            modifier = modifier
                .padding(vertical = 28.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.width(48.dp),
                    text = when (timeState) {
                        TimeState.At -> stringResource(R.string.at)
                        TimeState.From -> stringResource(R.string.from)
                        TimeState.To -> stringResource(R.string.to)
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = time.formatToHHMM()) // 08:30
            }
            Row {
                if (isEditing) {
                    EditButton(
                        onEdit = editTimeDialogState::show,
                        label = stringResource(R.string.edit_time)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier,
                    text = time.formatToDate()
                )
            }
            Row {
                if (isEditing) {
                    EditButton(
                        onEdit = editDateDialogState::show,
                        label = stringResource(R.string.edit_date)
                    )
                }
            }
        }
    }
}
