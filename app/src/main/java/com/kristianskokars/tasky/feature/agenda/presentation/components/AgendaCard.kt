package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyAlertDialog
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.core.presentation.theme.LightGreen
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.lib.formatTime

private val agendaDropdownItems
    @Composable
    get() = listOf(
        stringResource(id = R.string.open),
        stringResource(id = R.string.edit),
        stringResource(id = R.string.delete)
    )

@Composable
fun AgendaCard(
    modifier: Modifier = Modifier,
    agenda: Agenda,
    onTaskIsDone: (taskId: String) -> Unit,
    onOpenClick: (agenda: Agenda) -> Unit,
    onEditClick: (agenda: Agenda) -> Unit,
    onDeleteClick: (id: String) -> Unit,
) {
    val backgroundColor = when (agenda) {
        is Agenda.Task -> Green
        is Agenda.Event -> LightGreen
        is Agenda.Reminder -> LightGray
    }
    val headingColor = when (agenda) {
        is Agenda.Task -> White
        is Agenda.Event -> Black
        is Agenda.Reminder -> Black
    }
    val textColor = when (agenda) {
        is Agenda.Task -> White
        is Agenda.Event -> DarkGray
        is Agenda.Reminder -> DarkGray
    }

    var isDropdownOpen by remember {
        mutableStateOf(false)
    }

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    if (showConfirmDeleteDialog) {
        TaskyAlertDialog(
            title = { Text(text = stringResource(id = R.string.delete_agenda_alert_dialog_title)) },
            text = { Text(text = stringResource(R.string.confirm_agenda_delete)) },
            onConfirm = { onDeleteClick(agenda.id) },
            onDismissRequest = { showConfirmDeleteDialog = false }
        )
    }

    CompositionLocalProvider(LocalContentColor provides textColor) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor)
                .fillMaxWidth()
                .height(124.dp)
                .padding(12.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.size(6.dp))

                Box(
                    modifier = Modifier
                        .clickable(
                            enabled = agenda is Agenda.Task,
                            onClick = { if (agenda is Agenda.Task) onTaskIsDone(agenda.id) }
                        )
                ) {
                    if (agenda.isDone) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_checked_circle),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_unchecked_circle),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = agenda.title,
                        color = headingColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (agenda.isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Box(contentAlignment = Alignment.TopStart) {
                        Icon(
                            // TODO: meet accessibility guidelines here
                            modifier = Modifier.clickable { isDropdownOpen = true },
                            painter = painterResource(id = R.drawable.ic_dropdown_menu),
                            contentDescription = stringResource(R.string.open_agenda_menu)
                        )
                        AgendaDropdownMenu(
                            items = agendaDropdownItems,
                            onItemClick = { index ->
                                  when (index) {
                                      0 -> onOpenClick(agenda)
                                      1 -> onEditClick(agenda)
                                      2 -> showConfirmDeleteDialog = true
                                  }
                            },
                            isExpanded = isDropdownOpen,
                            onDismissRequest = {
                                isDropdownOpen = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    Text(
                        text = agenda.description,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        lineHeight = 12.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = agenda.time.formatTime(),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AgendaCardPreview() {
    ScreenSurface {
        Column(
            modifier = Modifier
                .background(White)
                .padding(20.dp)
        ) {
            Agenda.previewValues.forEach { agenda ->
                AgendaCard(
                    agenda = agenda,
                    onTaskIsDone = {},
                    onOpenClick = {},
                    onEditClick = {},
                    onDeleteClick = {}
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}
