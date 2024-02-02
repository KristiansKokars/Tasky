package com.kristianskokars.tasky.core.presentation.components.agendacreation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kristianskokars.tasky.R

@Composable
fun EditIndicatorIcon(modifier: Modifier = Modifier, label: String? = null) {
    Icon(
        modifier = modifier
            .padding(8.dp),
        painter = painterResource(id = R.drawable.ic_chevron_right),
        contentDescription = label
    )
}
