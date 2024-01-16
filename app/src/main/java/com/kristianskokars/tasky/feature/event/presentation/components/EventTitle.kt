package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface

@Composable
fun EventTitle(title: String, isEditing: Boolean = false, onEditTitle: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_unchecked_circle),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        if (isEditing) {
            Spacer(modifier = Modifier.weight(1f))
            EditButton(onEdit = onEditTitle, label = stringResource(R.string.edit_title))
        }
    }
}

@Preview
@Composable
fun EventTitlePreview() {
    ScreenSurface {
        EventTitle(title = "Meeting", isEditing = true)
    }
}