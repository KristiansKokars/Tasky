package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun TaskyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth() // TODO: limit size to a specific breakpoint so it does not look that terrible on tablets
            .defaultMinSize(minHeight = 54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        onClick = onClick
    ) {
        content()
    }
}