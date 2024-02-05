package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.event.domain.model.AttendeeStatusFilter

fun LazyListScope.visitorsSection(
    onSwitchStatusFilter: (AttendeeStatusFilter) -> Unit,
    selectedStatusFilter: AttendeeStatusFilter,
    isEditing: Boolean = false,
    onEditVisitors: () -> Unit = {},
    creatorUserId: String? = null,
    canRemoveAttendee: Boolean,
    goingAttendees: List<Attendee>,
    notGoingAttendees: List<Attendee>,
    onRemoveAttendee: (Attendee) -> Unit
) {
    item {
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.visitors),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            if (isEditing) {
                Spacer(modifier = Modifier.size(16.dp))
                IconButton(
                    modifier = Modifier
                        .background(LightGray, shape = RoundedCornerShape(4.dp))
                        .size(36.dp),
                    onClick = onEditVisitors
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = Gray
                    )
                }
            }
        }
    }
    item {
       AttendeeStatusTabRow(
           onSwitchStatusFilter = onSwitchStatusFilter,
           selectedStatusFilter = selectedStatusFilter
       )
    }
    if (selectedStatusFilter == AttendeeStatusFilter.GOING || selectedStatusFilter == AttendeeStatusFilter.ALL) {
        item {
            AttendeeSectionTitle(text = stringResource(R.string.going))
        }
        items(goingAttendees, key = { it.userId }) { attendee ->
            VisitorCard(
                attendee = attendee,
                isCreator = attendee.userId == creatorUserId,
                onRemoveAttendee = onRemoveAttendee,
                canRemoveAttendee = canRemoveAttendee
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
    if (selectedStatusFilter == AttendeeStatusFilter.NOT_GOING || selectedStatusFilter == AttendeeStatusFilter.ALL) {
        item {
            AttendeeSectionTitle(text = stringResource(R.string.not_going))
        }
        items(notGoingAttendees, key = { it.userId }) { attendee ->
            VisitorCard(
                attendee = attendee,
                isCreator = attendee.userId == creatorUserId,
                onRemoveAttendee = onRemoveAttendee,
                canRemoveAttendee = canRemoveAttendee
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
private fun AttendeeSectionTitle(text: String) {
    Spacer(modifier = Modifier.size(20.dp))
    Text(
        text = text,
        color = DarkGray,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.size(16.dp))
}
