package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import journal.JournalResponse
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.LocalDate

@Composable
fun CalendarWithHeader(appState: AppState) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val coroutineScope = rememberCoroutineScope()

    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(currentMonth) {
        isLoading = true
        coroutineScope.launch {
            val journals = appState.userStrategy?.getJournalByMonth(appState, currentMonth.monthValue, currentMonth.year)
            val journalDates = journals?.mapNotNull {
                LocalDate.of(it.year, it.month, it.day)
            } ?: listOf()
            appState.currMonthJournals.value = journalDates
            isLoading = false
        }
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Column {
        MonthNavigationHeader(
            currentMonth = currentMonth,
            onMonthChanged = { newMonth ->
                currentMonth = newMonth
                coroutineScope.launch {
                    state.scrollToMonth(newMonth)
                }
            }
        )
        HorizontalDivider()
        Calendar(currentMonth, state, appState, isLoading)
    }
}

@Composable
fun Calendar(
    currentMonth: YearMonth = remember { YearMonth.now() },
    calendarState: CalendarState,
    appState: AppState,
    isLoading: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalCalendar(
            state = calendarState,
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
                Day(day, currentMonth, appState)
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}



@Composable
fun Day(
    day: CalendarDay,
    currentMonth: YearMonth,
    appState: AppState,
) {
    val lightGreen = Color(0xFFD6F3D8)
    val lightGray = Color(0xFFB0B0B0)

    val coroutineScope = rememberCoroutineScope()
    val isInCurrentMonth = day.date.month == currentMonth.month && day.date.year == currentMonth.year
    val backgroundColor = when {
        day.date in appState.currMonthJournals.value -> lightGreen
        day.date.isAfter(LocalDate.now()) && isInCurrentMonth -> lightGray
        day.date.month == currentMonth.month -> Color.White
        else -> Color.LightGray
    }

    if (isInCurrentMonth) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(enabled = !day.date.isAfter(LocalDate.now())) {
                    appState.selectedDate.value = day.date

                    coroutineScope.launch {
                        val journalRes: JournalResponse? = appState.userStrategy?.getJournalByDate(
                            appState = appState,
                            day = day.date.dayOfMonth,
                            month = day.date.monthValue,
                            year = day.date.year
                        )

                        if (journalRes == null) {
                            appState.pageState.value = PageStates.JOURNAL_STEP2
                        } else {
                            appState.pastJournalEntry.value = journalRes.content
                            appState.pastSelectedMoods.value = journalRes.moods
                            appState.pastDate.value = LocalDate.of(journalRes.year, journalRes.month, journalRes.day)
                            appState.pageState.value = PageStates.PAST_JOURNAL
                        }
                    }

                },
            contentAlignment = Alignment.Center
        ) {
            if (backgroundColor != Color.Transparent) {
                Canvas(modifier = Modifier.size(42.dp)) {
                    drawCircle(
                        color = backgroundColor,
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.minDimension / 2
                    )
                }
            }

            Text(
                text = day.date.dayOfMonth.toString(),
                color = Color.Black
            )
        }
    } else {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = Color.LightGray
            )
        }
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
    val now = YearMonth.now()
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

        val isFutureDisabled = currentMonth >= now
        IconButton(
            onClick = { if (!isFutureDisabled) onMonthChanged(currentMonth.plusMonths(1)) },
            enabled = !isFutureDisabled
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month", tint = if (isFutureDisabled) Color.Gray else Color.Black)
        }
    }
}

