package com.kristianskokars.tasky.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.kristianskokars.tasky.core.presentation.theme.Red
import com.kristianskokars.tasky.destinations.RegisterScreenDestination
import com.kristianskokars.tasky.feature.auth.domain.model.LoginError
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@RootNavGraph(start = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreenContent(navigator, state, viewModel::onEvent)
}

@Composable
private fun LoginScreenContent(
    navigator: DestinationsNavigator,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    val createAnAccountString = buildAnnotatedString {
        var start = 0
        var end = 0

        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
            withStyle(style = SpanStyle(color = Gray)) {
                append(stringResource(R.string.dont_have_an_account))
            }
            withStyle(style = SpanStyle(color = Purple)) {
                start = length
                append(stringResource(R.string.sign_up))
                end = length
            }
        }

        addStringAnnotation("navigate", "signup", start, end)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                style = BannerHeadingStyle
            )
        }
        TaskySurface {
            Spacer(modifier = Modifier.size(54.dp))
            TaskyTextField(
                text = state.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { onEvent(LoginEvent.OnEmailChange(it)) },
                placeholder = { Text(stringResource(R.string.email_address)) },
                trailingIcon = if (state.isEmailValid == true) {
                    {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = stringResource(R.string.email_address_is_valid),
                            tint = Green
                        )
                    }
                } else {
                    null
                },
                isError = state.isEmailValid == false
            )
            Spacer(modifier = Modifier.size(16.dp))
            TaskyTextField(
                text = state.password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { onEvent(LoginEvent.OnPasswordChange(it)) },
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                placeholder = { Text(stringResource(R.string.password)) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(LoginEvent.TogglePasswordVisibility) }) {
                        if (state.isPasswordVisible) {
                            Icon(
                                painterResource(R.drawable.ic_visibility_on),
                                contentDescription = stringResource(R.string.hide_password),
                                tint = LighterGray
                            )
                        } else {
                            Icon(
                                painterResource(R.drawable.ic_visibility_off),
                                contentDescription = stringResource(
                                    R.string.show_password
                                ),
                                tint = LighterGray
                            )
                        }
                    }
                },
                isError = state.isPasswordValid == false
            )
            Spacer(modifier = Modifier.size(24.dp))
            TaskyButton(enabled = state.canLogin, onClick = { onEvent(LoginEvent.Login) }) {
                Text(text = stringResource(R.string.log_in))
            }
            Spacer(modifier = Modifier.size(24.dp))
            LoginResultBox(loginResult = state.loginResult)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                ClickableText(
                    modifier = Modifier.padding(vertical = 80.dp),
                    text = createAnAccountString,
                    onClick = { offset ->
                        // TODO: this may need an increased range, otherwise taps do not feel great
                        createAnAccountString
                            .getStringAnnotations("navigate", offset, offset)
                            .firstOrNull()?.item ?: return@ClickableText

                        navigator.navigate(RegisterScreenDestination)
                    }
                )
            }
        }
    }
}

// TODO: nothing in the design about showing this, so temporary implementation of text for now until a later ticket
@Composable
private fun LoginResultBox(loginResult: LoginResult) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (loginResult) {
            is LoginResult.Error -> when (loginResult.error) {
                is LoginError.NetworkError -> Text(
                    text = stringResource(R.string.network_error_check_your_internet_connection),
                    textAlign = TextAlign.Center,
                    color = Red
                )

                is LoginError.ServerError -> Text(
                    text = stringResource(R.string.server_error_try_again_later),
                    textAlign = TextAlign.Center,
                    color = Red
                )

                is LoginError.WriteToLocalStorageError -> Text(
                    text = stringResource(
                        R.string.failed_to_write_data_to_local_device_is_your_storage_full
                    ),
                    textAlign = TextAlign.Center,
                    color = Red
                )
            }
            else -> {}
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    ScreenSurface {
        LoginScreenContent(
            navigator = EmptyDestinationsNavigator,
            state = LoginState(),
            onEvent = {}
        )
    }
}
