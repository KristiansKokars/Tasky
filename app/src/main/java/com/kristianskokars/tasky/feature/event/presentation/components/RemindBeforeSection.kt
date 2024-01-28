package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun RemindBeforeSection(
    modifier: Modifier = Modifier,
    remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
    isEditing: Boolean = false,
    onChangeRemindAtTime: (RemindAtTime) -> Unit = {},
) {
    var isDropdownExpanded by remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = modifier
                .padding(vertical = 28.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(color = LightGray, shape = RoundedCornerShape(5.dp))
                    .size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = stringResource(R.string.reminder_at),
                    tint = Gray
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(text = remindAtTime.toUIString())
            if (isEditing) {
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    EditIndicatorIcon(
                        modifier = Modifier.clickable { isDropdownExpanded = true },
                        label = stringResource(R.string.edit_reminder)
                    )
                    RemindAtDropdownMenu(
                        items = RemindAtTime.remindAtTimes,
                        isExpanded = isDropdownExpanded,
                        onItemClick = { newRemindAtTime ->
                            onChangeRemindAtTime(newRemindAtTime)
                            isDropdownExpanded = false
                        },
                        onDismissRequest = {
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RemindAtDropdownMenu(
    items: List<RemindAtTime>,
    onItemClick: (RemindAtTime) -> Unit = {},
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
                items.forEach { remindAt ->
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Black),
                        text = {
                            Text(text = remindAt.toUIString())
                        },
                        onClick = { onItemClick(remindAt) }
                    )
                }
            }
        }
    }
}
