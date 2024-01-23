package com.kristianskokars.tasky.feature.event.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGreen
import com.kristianskokars.tasky.core.presentation.util.fillParentWidth
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaBadge
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaDescription
import com.kristianskokars.tasky.feature.event.presentation.components.AgendaTopBar
import com.kristianskokars.tasky.feature.event.presentation.components.EventTitle
import com.kristianskokars.tasky.feature.event.presentation.components.PhotosSection
import com.kristianskokars.tasky.feature.event.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.event.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.feature.event.presentation.components.VisitorsSection
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AppGraph
@Destination
@Composable
fun CreateEventScreen(
    navigator: DestinationsNavigator,

    ) {
    CreateEventScreenContent(navigator = navigator)
}

@Composable
private fun CreateEventScreenContent(navigator: DestinationsNavigator) {
    Scaffold(
        topBar = {
            AgendaTopBar(
                title = "01 MARCH 2022",
                isEditingTitle = "01 MARCH 2022",
            )
        },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            TaskySurface(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.size(32.dp))
                AgendaBadge(text = stringResource(id = R.string.event), badgeColor = LightGreen)
                Spacer(modifier = Modifier.size(8.dp))
                EventTitle(
                    modifier = Modifier.fillParentWidth(16.dp),
                    title = "Meeting",
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination(""))
                    },
                    isEditing = true
                )
                Spacer(modifier = Modifier.size(8.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                AgendaDescription(
                    text = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint.",
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination(""))
                    }
                )
                Spacer(modifier = Modifier.size(32.dp))
                PhotosSection()
                Spacer(modifier = Modifier.size(32.dp))
                TaskyDivider()
                TaskyTimeSection()
                TaskyDivider()
                TaskyTimeSection()
                TaskyDivider()
                RemindBeforeSection()
                TaskyDivider()
                VisitorsSection()
                Spacer(modifier = Modifier.size(44.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = Gray),
                        onClick = { /*TODO*/ })
                    {
                        Text(text = stringResource(R.string.delete_event))
                    }
                }
            }
        }
    }
}

@Preview(heightDp = 2000)
@Composable
private fun CreateEventScreenPreview() {
    ScreenSurface {
        CreateEventScreenContent(navigator = EmptyDestinationsNavigator)
    }
}
