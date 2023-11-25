package com.kristianskokars.tasky.feature.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.LighterGray
import com.kristianskokars.tasky.core.presentation.theme.Red
import com.kristianskokars.tasky.feature.auth.data.model.RegisterError
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterScreenContent(
        navigator,
        state,
        viewModel::onEvent
    )
}

@Composable
fun RegisterScreenContent(
    navigator: DestinationsNavigator,
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.create_your_account),
                style = BannerHeadingStyle
            )
        }

        TaskySurface {
            Spacer(modifier = Modifier.size(54.dp))
            TaskyTextField(
                text = state.name,
                onValueChange = { onEvent(RegisterEvent.OnNameChange(it)) },
                placeholder = { Text(stringResource(R.string.name)) },
                trailingIcon = if (state.isNameValid == true) {
                    {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = stringResource(R.string.name_is_valid),
                            tint = Green
                        )
                    }
                } else {
                    null
                },
                isError = state.isNameValid == false
            )
            Spacer(modifier = Modifier.size(16.dp))
            TaskyTextField(
                text = state.email,
                onValueChange = { onEvent(RegisterEvent.OnEmailChange(it)) },
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
                onValueChange = { onEvent(RegisterEvent.OnPasswordChange(it)) },
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                placeholder = { Text(stringResource(R.string.password)) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(RegisterEvent.TogglePasswordVisibility) }) {
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
            TaskyButton(onClick = {
                onEvent(RegisterEvent.Register)
            }, enabled = state.canRegister) {
                Text(text = stringResource(R.string.get_started))
            }
            Spacer(modifier = Modifier.size(24.dp))
            RegisterResultBox(registerResult = state.registerResult)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(vertical = 80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Black)
                        .size(56.dp),
                    onClick = navigator::navigateUp
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = stringResource(R.string.go_back_to_login)
                    )
                }
            }
        }
    }
}

// TODO: nothing in the design about showing this, so temporary implementation of text for now until a later ticket
@Composable
private fun RegisterResultBox(registerResult: RegisterResult) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (registerResult) {
            is RegisterResult.Success -> Text(
                text = stringResource(R.string.registered_successfully),
                textAlign = TextAlign.Center,
                color = Green
            )
            is RegisterResult.Error -> when (registerResult.error) {
                is RegisterError.NetworkError -> Text(
                    text = stringResource(R.string.network_error_check_your_internet_connection),
                    textAlign = TextAlign.Center,
                    color = Red
                )
                is RegisterError.ServerError -> Text(
                    text = stringResource(R.string.server_error_try_again_later),
                    textAlign = TextAlign.Center,
                    color = Red
                )
            }
            is RegisterResult.NoResult -> {}
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    ScreenSurface {
        RegisterScreenContent(
            navigator = EmptyDestinationsNavigator,
            state = RegisterState(),
            onEvent = {}
        )
    }
}
