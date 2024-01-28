package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.LoadingSpinner
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.components.TaskyTextField
import com.kristianskokars.tasky.core.presentation.components.ThemeSurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun AddVisitorDialog(
    isDialogOpen: Boolean,
    isCheckingIfAttendeeExists: Boolean,
    onDismissRequest: () -> Unit,
    onAddVisitor: (email: String) -> Unit,
) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismissRequest) {
            AddVisitorDialogContent(
                isCheckingIfAttendeeExists = isCheckingIfAttendeeExists,
                onDismissRequest = onDismissRequest,
                onAddVisitor = onAddVisitor
            )
        }
    }
}

@Composable
fun AddVisitorDialogContent(
    isCheckingIfAttendeeExists: Boolean,
    onDismissRequest: () -> Unit,
    onAddVisitor: (email: String) -> Unit
) {
    var emailAddress by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .background(White, RoundedCornerShape(10.dp))
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onDismissRequest) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(R.string.close_add_visitor_dialog),
                    tint = Black
                )
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Text(text = stringResource(R.string.add_visitor), color = Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(30.dp))
        TaskyTextField(text = emailAddress, onValueChange = { emailAddress = it }, placeholder = { Text(text = stringResource(id = R.string.email_address)) })
        Spacer(modifier = Modifier.size(30.dp))
        TaskyButton(onClick = { onAddVisitor(emailAddress) }) {
            if (isCheckingIfAttendeeExists) {
                LoadingSpinner()
            } else {
                Text(text = stringResource(R.string.add), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
    }
}

@Preview
@Composable
fun AddVisitorPreview() {
    ThemeSurface {
        AddVisitorDialogContent(
            isCheckingIfAttendeeExists = false,
            onDismissRequest = {},
            onAddVisitor = {}
        )
    }
}