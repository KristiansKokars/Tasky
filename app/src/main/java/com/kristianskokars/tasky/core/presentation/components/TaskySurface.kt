package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun TaskySurface(
    modifier: Modifier = Modifier,
    showWhiteOverlay: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(30.dp, 30.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            if (showWhiteOverlay) {
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .fillMaxSize()
                        .background(
                            White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(30.dp, 30.dp)
                        )
                        .zIndex(1f)
                )
            }
            Column {
                content()
            }
        }
    }
}
