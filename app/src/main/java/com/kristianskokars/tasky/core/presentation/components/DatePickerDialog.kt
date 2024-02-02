package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun DatePickerDialog(
    allowedDateValidator: (LocalDate) -> Boolean = { true },
    dialogState: MaterialDialogState,
    onDateSelected: (LocalDate) -> Unit
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
        datepicker(
            allowedDateValidator = { allowedDateValidator(it.toKotlinLocalDate()) },
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Black,
                dateActiveBackgroundColor = Black,
            ),
            onDateChange = { date -> onDateSelected(date.toKotlinLocalDate()) }
        )
    }
}
