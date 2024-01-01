package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Light
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun TaskyDropdownMenu(
    items: List<String>,
    onItemClick: (index: Int) -> Unit = {},
    isExpanded: Boolean = false,
    onDismissRequest: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides Black) {
        Box {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onDismissRequest,
                modifier = Modifier.background(White)
            ) {
                items.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Black),
                        text = {
                            Text(text = text)
                        },
                        onClick = { onItemClick(index) }
                    )
                    if (index != items.size - 1) {
                        Divider(color = Light)
                    }
                }
            }
        }
    }
}
