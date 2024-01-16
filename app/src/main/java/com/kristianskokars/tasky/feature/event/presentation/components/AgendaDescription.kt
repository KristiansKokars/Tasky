package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R

@Composable
fun AgendaDescription(
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onEditDescription: () -> Unit = {},
    text: String,
) {
    Row(modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
        Text(modifier = Modifier.weight(3f), text = text)

        if (isEditing) {
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                EditButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onEdit = onEditDescription,
                    label = stringResource(R.string.edit_description)
                )
            }
        }
    }
}
