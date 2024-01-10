package com.kristianskokars.tasky.core.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.fillParentWidth(parentPadding: Dp): Modifier =
    layout { measurable, constraints ->
        val maxWidthWithPadding = constraints.maxWidth + parentPadding.roundToPx() * 2
        val placeable = measurable.measure(
            constraints.copy(
                minWidth = maxWidthWithPadding,
                maxWidth = maxWidthWithPadding
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }