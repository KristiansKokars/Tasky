package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun TaskyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth() // TODO: limit size to a specific breakpoint so it does not look that terrible on tablets
            .defaultMinSize(minHeight = 54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White,
            disabledContainerColor = LightGray,
            disabledContentColor = DarkGray
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        content()
    }
}

@Preview
@Composable
private fun TaskyButtonPreview() {
    ThemeSurface {
        Column(
            modifier = Modifier.background(White).padding(12.dp)
        ) {
            TaskyButton(onClick = { /*TODO*/ }, enabled = false) {
                Text(text = "Button")
            }
        }
    }
}
