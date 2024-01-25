package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Gray,
        trackColor = LightGray,
    )
}

@Preview
@Composable
private fun LoadingSpinnerPreview() {
    ThemeSurface {
        LoadingSpinner()
    }
}