package com.kristianskokars.tasky.feature.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.core.presentation.theme.DarkGray
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.Orange
import com.kristianskokars.tasky.lib.initial
import kotlinx.datetime.LocalDate

@Composable
fun TopDayRow(
    days: List<LocalDate>,
    selectedDayIndex: Int,
    onDayClick: (index: Int) -> Unit
) {
    val dayShape = remember { RoundedCornerShape(20.dp) }

    LazyRow(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        itemsIndexed(days) { index, day ->
            Day(
                modifier = Modifier
                    .clip(dayShape)
                    .clickable(
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                    ) { onDayClick(index) },
                dayShape = dayShape,
                letter = day.dayOfWeek.initial(),
                number = day.dayOfMonth,
                isSelected = index == selectedDayIndex
            )
        }
    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    dayShape: RoundedCornerShape,
    letter: String,
    number: Int,
    isSelected: Boolean
) {
    val selectedBackground = Modifier
        .clip(dayShape)
        .background(Orange)
    Column(
        modifier = modifier
            .then(if (isSelected) selectedBackground else Modifier)
            .width(48.dp)
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
