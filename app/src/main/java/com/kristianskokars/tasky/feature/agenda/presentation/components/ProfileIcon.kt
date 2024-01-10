package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.AvatarIcon
import com.kristianskokars.tasky.core.presentation.components.TaskyDropdownMenu
import com.kristianskokars.tasky.core.presentation.theme.Light
import com.kristianskokars.tasky.core.presentation.theme.LightBlue

@Composable
fun ProfileIcon(
    onLogOut: () -> Unit,
) {
    var isDropdownOpen by remember { mutableStateOf(false) }
    val logoutText = stringResource(R.string.logout)
    val dropdownItems = remember { listOf(logoutText) }

    Box(contentAlignment = Alignment.BottomCenter) {
        AvatarIcon(
            modifier = Modifier.clickable { isDropdownOpen = true },
            text = "AB", // TODO: make sure it is not hardcoded later
            textColor = LightBlue,
            containerColor = Light
        )
        TaskyDropdownMenu(
            items = dropdownItems,
            onItemClick = {
                isDropdownOpen = false
                onLogOut()
            },
            isExpanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false }
        )
    }
}
