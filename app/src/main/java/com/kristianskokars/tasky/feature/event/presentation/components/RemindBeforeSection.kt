package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightGray

@Composable
fun RemindBeforeSection(
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onEditReminder: () -> Unit = {}
) {
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
                    contentDescription = "Reminder at",
                    tint = Gray
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(text = "30 minutes before")
            if (isEditing) {
                Spacer(modifier = Modifier.weight(1f))
                EditButton(
                    onEdit = onEditReminder,
                    label = stringResource(R.string.edit_reminder)
                )
            }
        }
    }
}
