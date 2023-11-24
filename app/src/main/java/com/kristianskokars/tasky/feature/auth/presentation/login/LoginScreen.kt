package com.kristianskokars.tasky.feature.auth.presentation.login

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.components.TaskyTextField
import com.kristianskokars.tasky.core.presentation.theme.BannerHeadingStyle
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.LighterGray
import com.kristianskokars.tasky.core.presentation.theme.Purple

@Composable
fun LoginScreen(

) {
    LoginScreenContent()
}

@Composable
private fun LoginScreenContent() {
    var emailAddress by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                style = BannerHeadingStyle
            )
        }
        TaskySurface {
            Spacer(modifier = Modifier.size(54.dp))
            TaskyTextField(
                text = emailAddress,
                onValueChange = { emailAddress = it },
                placeholder = { Text(stringResource(R.string.email_address)) },
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
                text = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(stringResource(R.string.password)) },
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
