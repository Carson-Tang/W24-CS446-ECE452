package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.MutableState
import java.time.LocalDate
@Composable
fun CalendarWithHeader(pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column {
        MonthNavigationHeader(
            currentMonth = currentMonth,
            onMonthChanged = { newMonth ->
                currentMonth = newMonth
            }
        )
        HorizontalDivider()

        Calendar(currentMonth, pageState, selectedDate)
    }
}
@Composable
fun Calendar(currentMonth: YearMonth = remember { YearMonth.now() }, pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>) {
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        monthHeader = { month ->
            val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
            MonthHeader(daysOfWeek = daysOfWeek)
        },
        monthBody = { _, content ->
            Box(
                modifier = Modifier
                    .background(Color.White)
            ) {
                content()
            }
        },
        dayContent = { day ->
            Day(day, pageState, selectedDate)
        }
    )
}


@Composable
fun Day(day: CalendarDay, pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable {
                pageState.value = PageStates.JOURNAL_STEP2
                selectedDate.value = day.date
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .padding(top = 2.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.name.first().toString(),
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun MonthNavigationHeader(
    currentMonth: YearMonth,
    onMonthChanged: (YearMonth) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onMonthChanged(currentMonth.minusMonths(1)) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month", tint = Color.Black)
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        IconButton(onClick = { onMonthChanged(currentMonth.plusMonths(1)) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month", tint = Color.Black)
        }
    }
}

