package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray

@Composable
fun VisitorsSection(
    isEditing: Boolean = false,
    onEditVisitors: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Visitors",
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
        Row(
            modifier = Modifier.height(36.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TaskyButton(
                modifier = Modifier.weight(1f),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "All")
            }
            TaskyButton(
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Going")
            }
            TaskyButton(
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Not Going")
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Going",
            color = DarkGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            VisitorCard(isCreator = true)
            VisitorCard(isCreator = false)
            VisitorCard(isCreator = false)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Not going", color = DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.size(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            VisitorCard(isCreator = false)
            VisitorCard(isCreator = false)
        }
    }
}
