package com.kristianskokars.tasky.feature.agenda.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.components.TaskySurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Light
import com.kristianskokars.tasky.core.presentation.theme.LightBlue
import com.kristianskokars.tasky.core.presentation.theme.Orange
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda
import com.kristianskokars.tasky.feature.agenda.presentation.components.AgendaCard
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination

@AppGraph(start = true)
@Destination
@Composable
fun AgendaScreen() {
    AgendaScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MARCH",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_open_date_dropdown),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Light)
                            .padding(6.dp),
                        text = "AB",
                        color = LightBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            )
        },
        floatingActionButton = {
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
    ) { padding ->
        TaskySurface(modifier = Modifier.padding(padding)) {
            DaySelection()
            Spacer(modifier = Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Today", style = MaterialTheme.typography.headlineMedium)
            }
            Box {
                LazyColumn(
                    contentPadding = PaddingValues(top = 20.dp)
                ) {
                    items(Agenda.previewValues) { agenda ->
                        AgendaCard(agenda = agenda)
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
                Column {
                    Spacer(modifier = Modifier.size(150.dp))
                    TimeNeedle()
                }
            }
        }
    }
}

@Composable
private fun TimeNeedle() {
    Box(
        modifier = Modifier
            .background(Black)
            .fillMaxWidth()
            .height(2.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(Black, radius = 15f, center = Offset(0f, this.center.y))
        }
    }
}



private val days = listOf(
    "S" to 5,
    "M" to 6,
    "T" to 7,
    "W" to 8,
    "T" to 9,
    "F" to 10
)

@Composable
private fun DaySelection() {
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, (letter, number) ->
            Day(letter = letter, number = number, isSelected = index == 0)
        }
    }
}

@Composable
private fun Day(letter: String, number: Int, isSelected: Boolean) {
    val selectedBackground = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Orange)
    Column(
        modifier = Modifier
            .then(if (isSelected) selectedBackground else Modifier)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = letter,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) DarkGray else Gray
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "$number", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DarkGray)
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    ScreenSurface {
        AgendaScreenContent()
    }
}
