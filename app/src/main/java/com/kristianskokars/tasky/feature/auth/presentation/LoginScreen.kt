package com.kristianskokars.tasky.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.components.TaskyTextField
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.LighterGray
import com.kristianskokars.tasky.core.presentation.theme.Purple

@Composable
fun LoginScreen() {
    LoginScreenContent()
}

@Composable
private fun LoginScreenContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            // TODO: see if this repeats and needs a separate style
            Text(
                text = stringResource(R.string.welcome_back),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
        TaskySurface {
            Spacer(modifier = Modifier.size(54.dp))
            TaskyTextField(
                text = "",
                onValueChange = {},
                placeholder = { Text("Email Address") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = stringResource(R.string.email_address_is_valid),
                        tint = Green
                    )
                }
            )
            Spacer(modifier = Modifier.size(16.dp))
            TaskyTextField(
                text = "",
                onValueChange = {},
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("Password") },
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_visibility_off),
                        contentDescription = stringResource(
                            R.string.show_password
                        ),
                        tint = LighterGray
                    )
                }
            )
            Spacer(modifier = Modifier.size(24.dp))
            TaskyButton(onClick = { /* TODO */ }) {
                Text(text = stringResource(R.string.log_in))
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 80.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                            withStyle(style = SpanStyle(color = Gray)) {
                                append(stringResource(R.string.dont_have_an_account))
                            }
                            withStyle(style = SpanStyle(color = Purple)) {
                                append(stringResource(R.string.sign_up))
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    ScreenSurface {
        LoginScreenContent()
    }
}