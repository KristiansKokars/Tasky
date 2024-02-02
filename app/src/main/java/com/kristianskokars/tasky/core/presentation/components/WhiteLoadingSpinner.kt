package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun WhiteLoadingSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = White,
        trackColor = Color.Transparent,
    )
}

@Preview
@Composable
private fun WhiteLoadingSpinnerPreview() {
    ThemeSurface {
        WhiteLoadingSpinner()
    }
}
