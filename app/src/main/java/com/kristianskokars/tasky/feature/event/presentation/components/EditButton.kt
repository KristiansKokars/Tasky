package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R

@Composable
fun EditButton(modifier: Modifier = Modifier, onEdit: () -> Unit, label: String) {
    Icon(
        modifier = modifier
            .padding(8.dp)
            .clickable(
                onClick = onEdit,
                role = Role.Button,
                onClickLabel = label
            ),
        painter = painterResource(id = R.drawable.ic_chevron_right),
        contentDescription = label
    )
}