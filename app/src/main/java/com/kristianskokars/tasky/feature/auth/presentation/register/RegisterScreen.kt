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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskyButton
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.components.TaskyTextField
import com.kristianskokars.tasky.core.presentation.theme.BannerHeadingStyle
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.Green
import com.kristianskokars.tasky.core.presentation.theme.LighterGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator
) {
    RegisterScreenContent(navigator)
}

@Composable
fun RegisterScreenContent(
    navigator: DestinationsNavigator
) {
    var name by remember {
        mutableStateOf("")
    }
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
                text = stringResource(R.string.create_your_account),
                style = BannerHeadingStyle
            )
        }
        TaskySurface {
            Spacer(modifier = Modifier.size(54.dp))
            TaskyTextField(
                text = name,
                onValueChange = { name = it },
                placeholder = { Text(stringResource(R.string.name)) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = stringResource(R.string.name_is_valid),
                        tint = Green
                    )
                }
            )
            Spacer(modifier = Modifier.size(16.dp))
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
                Text(text = stringResource(R.string.get_started))
            }
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

@Preview
@Composable
private fun RegisterScreenPreview() {
    ScreenSurface {
        RegisterScreenContent(
            navigator = EmptyDestinationsNavigator
        )
    }
}
