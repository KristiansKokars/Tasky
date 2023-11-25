package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.LightBlue
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.core.presentation.theme.Red

@Composable
fun TaskyTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val roundedCornerTextFieldShape = RoundedCornerShape(12.dp)
    var showFocusBorder by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { showFocusBorder = it.isFocused }
            .then(
                when {
                    isError -> Modifier.border(1.dp, Red, roundedCornerTextFieldShape)
                    showFocusBorder -> Modifier.border(1.dp, LightBlue, roundedCornerTextFieldShape)
                    else -> Modifier
                }
            ),
        value = text,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedTextColor = DarkGray,
            unfocusedTextColor = DarkGray,
            disabledTextColor = DarkGray,
            errorTextColor = DarkGray,
            focusedContainerColor = LightGray,
            unfocusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        isError = isError,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        shape = roundedCornerTextFieldShape,
        placeholder = placeholder
    )
}

@Preview
@Composable
private fun TaskyTextFieldPreview() {
    ThemeSurface {
        TaskyTextField(
            modifier = Modifier.padding(16.dp),
            text = "",
            onValueChange = {},
            placeholder = { Text(text = "Email Address") }
        )
    }
}

@Preview
@Composable
private fun TaskyTextFieldErrorPreview() {
    ThemeSurface {
        TaskyTextField(
            modifier = Modifier.padding(16.dp),
            text = "",
            onValueChange = {},
            placeholder = { Text(text = "Email Address") },
            isError = true
        )
    }
}
