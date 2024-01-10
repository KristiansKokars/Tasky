package com.kristianskokars.tasky.feature.createagenda.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R

// TODO: polish alignments here
@Composable
fun TaskyTimeSection(
    modifier: Modifier = Modifier,
    isEditing: Boolean = true,
    onEditTime: () -> Unit = {},
    onEditDate: () -> Unit = {},
) {
    Column {
        Row(
            modifier = modifier
                .padding(vertical = 28.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.width(48.dp), text = "To")
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "08:30")
            }
            Row {
                if (isEditing) {
                    EditButton(
                        onEdit = onEditTime,
                        label = stringResource(R.string.edit_time)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier,
                    text = "Jul 21 2022"
                )
            }
            Row {
                if (isEditing) {
                    EditButton(
                        onEdit = onEditDate,
                        label = stringResource(R.string.edit_date)
                    )
                }
            }
        }
    }
}
