package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun AvatarIcon(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = White,
    containerColor: Color = Gray
) {
    Text(
        modifier = modifier
            .padding(8.dp)
            .clip(CircleShape)
            .background(containerColor)
            .padding(6.dp),
        text = text,
        color = textColor,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    )
}