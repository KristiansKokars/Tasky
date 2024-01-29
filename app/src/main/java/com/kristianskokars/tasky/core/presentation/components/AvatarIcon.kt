package com.kristianskokars.tasky.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier
        .size(48.dp)
        .padding(8.dp)
        .clip(CircleShape)
        .background(containerColor)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
    }

}