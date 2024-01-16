package com.kristianskokars.tasky.feature.createagenda.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.feature.createagenda.presentation.components.EditToolbar
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination

@AppGraph
@Destination
@Composable
fun EditDescriptionScreen() {
    EditDescriptionScreenContent()
}

@Composable
private fun EditDescriptionScreenContent() {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. "))
    }

    CompositionLocalProvider(LocalContentColor provides Black) {
        Column(
            modifier = Modifier
                .background(White)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            EditToolbar(title = stringResource(id = R.string.edit_description))
            Spacer(modifier = Modifier.size(36.dp))
            BasicTextField(
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
        EditDescriptionScreenContent()
    }
}