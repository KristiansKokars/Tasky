package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.theme.Gray

@Composable
fun AgendaTitle(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean = false,
    padding: PaddingValues = PaddingValues(vertical = 16.dp),
    onEditTitle: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = isEditing,
                role = Role.Button,
                onClick = onEditTitle
            )
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_unchecked_circle),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))

        if (title.isEmpty()) {
            Text(
                text = stringResource(R.string.no_title),
                fontWeight = FontWeight.Bold,
                color = Gray.copy(alpha = 0.6f),
                fontSize = 26.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        } else {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (isEditing) {
            Spacer(modifier = Modifier.weight(1f))
            EditIndicatorIcon(label = stringResource(R.string.edit_title))
        }
    }
}

@Preview
@Composable
fun EventTitlePreview() {
    ScreenSurface {
        AgendaTitle(title = "Meeting", isEditing = true)
    }
}