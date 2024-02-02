package com.kristianskokars.tasky.core.presentation.components.agendacreation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.WhiteLoadingSpinner
import com.kristianskokars.tasky.core.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaTopBar(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean = false,
    editingTitle: String = "",
    isSaving: Boolean = false,
    onCloseClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }
        },
        title = {
            Text(
                text = if (isEditing) editingTitle.uppercase() else title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        actions = {
            when {
                isSaving -> {
                    Box(modifier = Modifier.width(IntrinsicSize.Max).padding(horizontal = 16.dp)) {
                        WhiteLoadingSpinner(modifier = Modifier.size(24.dp))
                    }
                }
                isEditing -> {
                    TextButton(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        onClick = onSaveClick
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            text = stringResource(R.string.save),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = White
                        )
                    }
                }
                else -> {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}
