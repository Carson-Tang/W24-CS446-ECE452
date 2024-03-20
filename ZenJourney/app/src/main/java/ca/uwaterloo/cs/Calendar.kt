package ca.uwaterloo.cs

import StatusResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.cs.api.JournalApiService
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import journal.JournalResponse
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@Composable
fun CalendarWithHeader(appState: AppState) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val coroutineScope = rememberCoroutineScope()

    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

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

        Calendar(currentMonth, state, appState)
    }
}

@Composable
fun Calendar(
    currentMonth: YearMonth = remember { YearMonth.now() },
    calendarState: CalendarState,
    appState: AppState
) {


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
}


@Composable
fun Day(
    day: CalendarDay,
    currentMonth: YearMonth,
    appState: AppState,
) {
    val coroutineScope = rememberCoroutineScope()

    val isInCurrentMonth = day.date.month == currentMonth.month && day.date.year == currentMonth.year
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable {
                appState.selectedDate.value = day.date
                coroutineScope.launch {
                    try {
                        val response = JournalApiService.getJournalByDateAndUser(
                            userId = appState.userId.value,
                            year = day.date.year,
                            month = day.date.monthValue,
                            day = day.date.dayOfMonth,
                            jwt = appState.dataStore.getJwt()
                        )

                        if (response.status == HttpStatusCode.OK) {
                            val journalResponse: JournalResponse = response.body()
                            appState.pastJournalEntry.value = journalResponse.content
                            appState.pastSelectedMoods.value = journalResponse.moods
                            appState.pastDate.value = LocalDate.of(journalResponse.year, journalResponse.month, journalResponse.day)
                            appState.pageState.value = PageStates.PAST_JOURNAL
                        } else if (response.status == HttpStatusCode.BadRequest || response.status == HttpStatusCode.NotFound) {
                            val statusResponse: StatusResponse = response.body()
                            // TODO: handle invalid query params or no data for date
                            println(statusResponse.body)
                            appState.pageState.value = PageStates.JOURNAL_STEP2
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (isInCurrentMonth) Color.Black else Color.Gray
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

