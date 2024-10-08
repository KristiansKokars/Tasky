package com.kristianskokars.tasky.core.presentation.components.agendacreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.core.presentation.theme.DarkGray

@Composable
fun AgendaBadge(
    modifier: Modifier = Modifier,
    badgeModifier: Modifier = Modifier,
    badgeColor: Color,
    text: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = badgeModifier
                .size(20.dp)
                .background(badgeColor, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            fontSize = 16.sp
        )
    }
}
