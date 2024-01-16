package com.kristianskokars.tasky.feature.createagenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.TaskyDivider
import com.kristianskokars.tasky.core.presentation.components.ThemeSurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun EditToolbar(
    modifier: Modifier = Modifier,
    title: String,
    onGoBack: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(vertical = 28.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onGoBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_small_left),
                    contentDescription = stringResource(R.string.go_back),
                    tint = Black
                )
            }
            Text(text = title.uppercase(), fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            TextButton(
                modifier = Modifier.width(IntrinsicSize.Max),
                onClick = onSave,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = stringResource(R.string.save),
                    color = Green,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
        TaskyDivider()
    }
}

@Preview
@Composable
fun EditToolbarPreview() {
    ThemeSurface {
        Column(modifier = Modifier
            .background(White)
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
            CompositionLocalProvider(LocalContentColor provides Black) {
                EditToolbar(title = "Preview")
            }
        }
    }
}