package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.lib.allTimesOfDay
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime

@Composable
fun TimePickerDialog(
    allowedTimeRange: ClosedRange<LocalTime> = allTimesOfDay(),
    dialogState: MaterialDialogState,
    onTimeSelected: (LocalTime) -> Unit
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                textStyle = LocalTextStyle.current.copy(color = Black),
                text = stringResource(
                    R.string.ok
                )
            )
            negativeButton(
                textStyle = LocalTextStyle.current.copy(color = Black),
                text = stringResource(
                    R.string.cancel
                )
            )
        }
    ) {
        timepicker(
            timeRange = allowedTimeRange.start.toJavaLocalTime() .. allowedTimeRange.endInclusive.toJavaLocalTime(),
            colors = TimePickerDefaults.colors(
                selectorColor = Black,
                inactivePeriodBackground = LightGray,
                inactiveBackgroundColor = LightGray,
                activeBackgroundColor = Black,
            ),
            onTimeChange = { time -> onTimeSelected(time.toKotlinLocalTime()) }
        )
    }
}