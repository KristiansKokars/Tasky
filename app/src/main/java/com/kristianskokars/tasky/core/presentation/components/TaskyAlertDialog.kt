package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Red
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun TaskyAlertDialog(
    title: @Composable (() -> Unit),
    text: @Composable (() -> Unit),
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        containerColor = White,
        textContentColor = Black,
        titleContentColor = Black,
        iconContentColor = Red,
        onDismissRequest = onDismissRequest,
        icon = { Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = null)},
        dismissButton = { TextButton(onClick = onDismissRequest, colors = ButtonDefaults.textButtonColors(contentColor = Black)) {
            Text(text = stringResource(id = R.string.cancel))
        }
        },
        confirmButton = { TextButton(onClick = onConfirm, colors = ButtonDefaults.textButtonColors(contentColor = Red)) {
            Text(text = stringResource(id = R.string.ok))
        } },
        title = title,
        text = text
    )
}
