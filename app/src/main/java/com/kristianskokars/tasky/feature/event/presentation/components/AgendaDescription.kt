package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Gray

@Composable
fun AgendaDescription(
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onEditDescription: () -> Unit = {},
    padding: PaddingValues = PaddingValues(vertical = 16.dp),
    text: String,
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = isEditing,
                role = Role.Button,
                onClick = onEditDescription
            )
            .padding(padding)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        if (text.isEmpty()) {
            Text(modifier = Modifier.weight(3f), text = stringResource(R.string.no_description_added), color = Gray.copy(alpha = 0.6f))
        } else {
            Text(modifier = Modifier.weight(3f), text = text)
        }

        if (isEditing) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                EditIndicatorIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    label = stringResource(R.string.edit_description)
                )
            }
        }
    }
}
