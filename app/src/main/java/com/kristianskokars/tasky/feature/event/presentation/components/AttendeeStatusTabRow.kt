package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ThemeSurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.feature.event.domain.model.AttendeeStatusFilter

@Composable
fun AttendeeStatusTabRow(
    modifier: Modifier = Modifier,
    onSwitchStatusFilter: (AttendeeStatusFilter) -> Unit,
    selectedStatusFilter: AttendeeStatusFilter,
) {
    Row(
        modifier = modifier.height(36.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AttendeeStatusTabButton(
            modifier = Modifier.weight(1f),
            isActive = selectedStatusFilter == AttendeeStatusFilter.ALL,
            onClick = { onSwitchStatusFilter(AttendeeStatusFilter.ALL) }
        ) {
            Text(text = stringResource(R.string.all))
        }
        AttendeeStatusTabButton(
            modifier = Modifier.weight(1f),
            isActive = selectedStatusFilter == AttendeeStatusFilter.GOING,
            onClick = { onSwitchStatusFilter(AttendeeStatusFilter.GOING) }
        ) {
            Text(text = stringResource(R.string.going))
        }
        AttendeeStatusTabButton(
            modifier = Modifier.weight(1f),
            isActive = selectedStatusFilter == AttendeeStatusFilter.NOT_GOING,
            onClick = { onSwitchStatusFilter(AttendeeStatusFilter.NOT_GOING) }
        ) {
            Text(text = stringResource(R.string.not_going))
        }
    }
}

@Composable
private fun AttendeeStatusTabButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isActive: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Black else LightGray,
            contentColor = if (isActive) White else DarkGray,
        ),
        onClick = onClick
    ) {
        content()
    }
}


@Preview
@Composable
fun TaskyTabRowPreview() {
    ThemeSurface {
        Box(modifier = Modifier.background(White).padding(20.dp)) {
            AttendeeStatusTabRow(
                onSwitchStatusFilter = {},
                selectedStatusFilter = AttendeeStatusFilter.ALL
            )
        }
    }
}
