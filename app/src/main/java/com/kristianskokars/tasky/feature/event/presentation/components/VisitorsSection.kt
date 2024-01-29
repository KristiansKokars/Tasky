package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.feature.event.domain.model.Attendee

fun LazyListScope.visitorsSection(
    isEditing: Boolean = false,
    onEditVisitors: () -> Unit = {},
    creator: Attendee? = null,
    attendees: List<Attendee>
) {
    item {
        Row(
            modifier = Modifier.padding(vertical = 40.dp),
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
        Row(
            modifier = Modifier.height(36.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TaskyButton(
                modifier = Modifier.weight(1f),
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(R.string.all))
            }
            TaskyButton(
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(R.string.going))
            }
            TaskyButton(
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(R.string.not_going))
            }
        }
    }
    item {
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.going),
            color = DarkGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
//    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//
//    }
    item {
        if (creator != null) {
            VisitorCard(attendee = creator, isCreator = true)
        }
    }
    items(attendees, key = { it.userId }) { attendee ->
        VisitorCard(attendee, isCreator = false)
    }
    item {
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = stringResource(R.string.not_going), color = DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.size(16.dp))
    }
//    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//
//    }

    items(attendees, key = { it.userId }) { attendee ->
        VisitorCard(attendee, isCreator = false)
    }
}
