package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White

@Composable
fun AddAgendaButton() {
    FloatingActionButton(
        containerColor = Black,
        contentColor = White,
        onClick = { /*TODO*/ }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                R.string.add_new_agenda_item
            )
        )
    }
}
