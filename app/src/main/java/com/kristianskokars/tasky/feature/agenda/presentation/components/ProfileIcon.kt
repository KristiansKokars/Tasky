package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
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
        Text(
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(Light)
                .padding(6.dp)
                .clickable { isDropdownOpen = true },
            text = "AB",
            color = LightBlue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
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
