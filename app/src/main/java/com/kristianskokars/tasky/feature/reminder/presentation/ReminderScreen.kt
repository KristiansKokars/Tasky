package com.kristianskokars.tasky.feature.reminder.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.feature.createagenda.presentation.components.AgendaBadge
import com.kristianskokars.tasky.feature.createagenda.presentation.components.AgendaDescription
import com.kristianskokars.tasky.feature.createagenda.presentation.components.AgendaTopBar
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EventTitle
import com.kristianskokars.tasky.feature.createagenda.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.createagenda.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AppGraph
@Destination
@Composable
fun ReminderScreen(navigator: DestinationsNavigator) {
    ReminderScreenContent(navigator = navigator)
}

@Composable
private fun ReminderScreenContent(navigator: DestinationsNavigator) {
    Scaffold(
        topBar = {
            AgendaTopBar(
                isEditing = true,
                isEditingTitle = stringResource(R.string.edit_reminder).uppercase()
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            TaskySurface(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.size(32.dp))
                AgendaBadge(
                    text = stringResource(id = R.string.reminder),
                    badgeColor = LightGray,
                    badgeModifier = Modifier.border(1.dp, Gray, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.size(32.dp))
                EventTitle(
                    title = "Project X",
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination)
                    }
                )
                Spacer(modifier = Modifier.size(24.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                AgendaDescription(
                    text = "Weekly plan\nRole distribution",
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination)
                    }
                )
                Spacer(modifier = Modifier.size(20.dp))
                TaskyDivider()
                TaskyTimeSection()
                TaskyDivider()
                RemindBeforeSection()
                TaskyDivider()
                Spacer(modifier = Modifier.weight(1f))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = Gray),
                        onClick = { /*TODO*/ })
                    {
                        Text(
                            text = stringResource(R.string.delete_reminder),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(32.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ReminderScreenPreview() {
    ScreenSurface {
        ReminderScreenContent(navigator = EmptyDestinationsNavigator)
    }
}