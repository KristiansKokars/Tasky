package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kristianskokars.tasky.core.presentation.theme.Light

@Composable
fun TaskyDivider(modifier: Modifier = Modifier) {
    Divider(modifier = modifier, color = Light)
}