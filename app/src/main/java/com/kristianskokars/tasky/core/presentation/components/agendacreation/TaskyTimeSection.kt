package com.kristianskokars.tasky.core.presentation.components.agendacreation

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.DatePickerDialog
import com.kristianskokars.tasky.core.presentation.components.TimePickerDialog
import com.kristianskokars.tasky.lib.allTimesOfDay
import com.kristianskokars.tasky.lib.formatToDate
import com.kristianskokars.tasky.lib.formatToHHMM
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

sealed interface TimeState {
    data object At : TimeState
    data object From : TimeState
    data object To : TimeState
}

// TODO: polish alignments here
@Composable
fun TaskyTimeSection(
    modifier: Modifier = Modifier,
    dateTime: LocalDateTime,
    timeState: TimeState = TimeState.At,
    isEditing: Boolean = false,
    allowedTimeRange: ClosedRange<LocalTime> = allTimesOfDay(),
    allowedDateValidator: (LocalDate) -> Boolean = { true },
    onTimeSelected: (LocalTime) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val editTimeDialogState = rememberMaterialDialogState()
    val editDateDialogState = rememberMaterialDialogState()

    TimePickerDialog(
        initialTime = dateTime.time,
        allowedTimeRange = allowedTimeRange,
        dialogState = editTimeDialogState,
        onTimeSelected = onTimeSelected
    )
    DatePickerDialog(
        initialDate = dateTime.date,
        allowedDateValidator = allowedDateValidator,
        dialogState = editDateDialogState,
        onDateSelected = onDateSelected
    )

    Column {
        Row(
            modifier = modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = isEditing,
                        onClick = editTimeDialogState::show,
                        role = Role.Button,
                        onClickLabel = stringResource(id = R.string.edit_time)
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width(48.dp),
                    text = when (timeState) {
                        TimeState.At -> stringResource(R.string.at)
                        TimeState.From -> stringResource(R.string.from)
                        TimeState.To -> stringResource(R.string.to)
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = dateTime.formatToHHMM())
                Spacer(modifier = Modifier.size(36.dp))
                if (isEditing) {
                    EditIndicatorIcon(
                        label = stringResource(R.string.edit_time)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = isEditing,
                        onClick = editDateDialogState::show,
                        role = Role.Button,
                        onClickLabel = stringResource(id = R.string.edit_date)
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateTime.formatToDate()
                )
                Spacer(modifier = Modifier.size(36.dp))
                if (isEditing) {
                    EditIndicatorIcon(
                        label = stringResource(R.string.edit_date)
                    )
                }
            }
        }
    }
}
