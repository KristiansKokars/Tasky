package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kristianskokars.tasky.core.presentation.theme.Black

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Black,
        trackColor = Color.Transparent,
    )
}

@Preview
@Composable
private fun LoadingSpinnerPreview() {
    ThemeSurface {
        LoadingSpinner()
    }
}
