package com.kristianskokars.tasky.feature.createagenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.AvatarIcon
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.LightBlue
import com.kristianskokars.tasky.core.presentation.theme.LightGray

@Composable
fun VisitorCard(
    isCreator: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray, shape = RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarIcon(text = "AA")
        Text(text = "Ann Allen", color = DarkGray, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(modifier = Modifier.weight(1f))
        if (isCreator) {
            Text(
                text = "creator",
                fontWeight = FontWeight.Medium,
                color = LightBlue
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Remove visitor",
                tint = DarkGray
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}