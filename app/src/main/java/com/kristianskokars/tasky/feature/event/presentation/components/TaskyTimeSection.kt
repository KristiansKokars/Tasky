package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.clickable
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
import com.kristianskokars.tasky.core.presentation.components.DatePickerDialog
import com.kristianskokars.tasky.core.presentation.components.TimePickerDialog
import com.kristianskokars.tasky.lib.formatToDate
import com.kristianskokars.tasky.lib.formatToHHMM
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
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
    onTimeSelected: (LocalTime) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val editTimeDialogState = rememberMaterialDialogState()
    val editDateDialogState = rememberMaterialDialogState()

    TimePickerDialog(
        dialogState = editTimeDialogState,
        onTimeSelected = onTimeSelected
    )
    DatePickerDialog(
        dialogState = editDateDialogState,
        onDateSelected = onDateSelected
    )

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
                    EditIndicatorIcon(
                        modifier = Modifier.clickable(onClick = editTimeDialogState::show),
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
                    EditIndicatorIcon(
                        modifier = Modifier.clickable(onClick = editDateDialogState::show),
                        label = stringResource(R.string.edit_date)
                    )
                }
            }
        }
    }
}
