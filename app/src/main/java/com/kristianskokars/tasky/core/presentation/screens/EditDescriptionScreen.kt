package com.kristianskokars.tasky.core.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.agendacreation.EditToolbar
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@AppGraph
@Destination
@Composable
fun EditDescriptionScreen(
    startingDescription: String,
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<String>
) {
    EditDescriptionScreenContent(startingDescription, navigator, resultNavigator)
}

@Composable
private fun EditDescriptionScreenContent(
    startingDescription: String,
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<String>
) {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(startingDescription))
    }

    CompositionLocalProvider(LocalContentColor provides Black) {
        Column(
            modifier = Modifier
                .background(White)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            EditToolbar(
                title = stringResource(id = R.string.edit_description),
                onSave = { resultNavigator.navigateBack(result = value.text) },
                onGoBack = navigator::navigateUp
            )
            Spacer(modifier = Modifier.size(36.dp))
            BasicTextField(
                modifier = Modifier.fillMaxSize(),
                value = value,
                onValueChange = { value = it },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp)
            )
        }
    }
}

@Preview
@Composable
fun EditDescriptionScreenPreview() {
    ScreenSurface {
        EditDescriptionScreenContent(
            startingDescription = "Description here",
            navigator = EmptyDestinationsNavigator,
            resultNavigator = EmptyResultBackNavigator()
        )
    }
}
