package com.kristianskokars.tasky.feature.createagenda.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.LightGreen
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EventBadge
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EventDescription
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EventTitle
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EventTopBar
import com.kristianskokars.tasky.feature.createagenda.presentation.components.PhotosSection
import com.kristianskokars.tasky.feature.createagenda.presentation.components.RemindBeforeSection
import com.kristianskokars.tasky.feature.createagenda.presentation.components.TaskyTimeSection
import com.kristianskokars.tasky.feature.createagenda.presentation.components.VisitorsSection
import com.kristianskokars.tasky.feature.destinations.EditDescriptionScreenDestination
import com.kristianskokars.tasky.feature.destinations.EditTitleScreenDestination
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AppGraph
@Destination
@Composable
fun CreateEventScreen(navigator: DestinationsNavigator) {
    CreateEventScreenContent(navigator = navigator)
}

@Composable
private fun CreateEventScreenContent(navigator: DestinationsNavigator) {
    Scaffold(
        topBar = { EventTopBar() },
    ) { padding ->
        CompositionLocalProvider(LocalContentColor provides Black) {
            TaskySurface(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.size(32.dp))
                EventBadge(text = stringResource(id = R.string.event), badgeColor = LightGreen)
                Spacer(modifier = Modifier.size(32.dp))
                EventTitle(
                    title = "Meeting",
                    onEditTitle = {
                        navigator.navigate(EditTitleScreenDestination)
                    }
                )
                Spacer(modifier = Modifier.size(24.dp))
                TaskyDivider()
                Spacer(modifier = Modifier.size(16.dp))
                EventDescription(
                    text = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint.",
                    onEditDescription = {
                        navigator.navigate(EditDescriptionScreenDestination)
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
