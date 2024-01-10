package com.kristianskokars.tasky.feature.createagenda.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTopBar() {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "01 MARCH 2022",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null
                )
            }
        }
    )
}