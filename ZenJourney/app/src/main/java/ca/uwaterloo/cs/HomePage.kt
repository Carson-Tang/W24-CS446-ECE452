package ca.uwaterloo.cs

import StatusResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.cs.api.JournalApiService
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import journal.JournalResponse
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun generateFeelingStatement(moods: List<String>): String {
    val moodMap = moodEmojisWithLabels.map { it.first to it.second.lowercase() }.toMap()
    val feelings = moods.mapNotNull { moodMap[it] }

    val feelingString = when {
        feelings.isEmpty() -> "No specific feelings"
        feelings.size == 1 -> "I'm feeling ${feelings.first()}"
        else -> "I'm feeling ${feelings.first()} and ..."
    }
    return feelingString
}

@Composable
fun WithInfo(today: java.time.LocalDate, todayJournalData: JournalResponse, appState: AppState) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "⛰\uFE0F\nYou're doing great today!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF3D3D3D)
        )
    }

    Column(
        modifier = Modifier
            .padding(top = 50.dp, start = 30.dp, end = 30.dp)
            .clickable(onClick = {
                // TODO: make sure this clickable actually shows today in past journal
                // might have to update appState
                appState.pageState.value = PageStates.PAST_JOURNAL
            })
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .padding(5.dp)
                .fillMaxWidth()
                .size(height = 60.dp, width = 140.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                // assumes len(todayJournalData.moods) >= 1, we only show
                // WithInfo if moods is not empty
                text = todayJournalData.moods[0],
                style = TextStyle(
                    fontSize = 40.sp
                )
            )
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Column {
                Text(
                    text = "Today, ${today.format(DateTimeFormatter.ofPattern("MMMM d"))}",
                    style = TextStyle(
                        color = Color(0xFF8FCAB5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = generateFeelingStatement(todayJournalData.moods),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
fun WithoutInfo() {
    Text(
        text = "\uD83E\uDD20\n Stay positive!",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        color = Color(0xFF3D3D3D),
    )

    Column(
        Modifier
            .padding(top = 48.dp)
    ) {
        Text(
            text = "Start logging your mood to\nsee your mood entries here!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF579981)
        )
    }
}

@Composable
fun HomePage(appState: AppState) {
    val coroutineScope = rememberCoroutineScope()
    val today = LocalDate.now()
    var todayJournalData by remember {
        mutableStateOf(
            JournalResponse(
                "",
                0,
                0,
                0,
                listOf(),
                "",
                ""
            )
        )
    }

    LaunchedEffect(Unit) {
        // This block will be executed when the composable is first displayed
        coroutineScope.launch {
            try {
                val response = JournalApiService.getJournalByDate(
                    today.year,
                    today.monthValue,
                    today.dayOfMonth,
                    appState.jwt.value
                )
                if (response.status == HttpStatusCode.OK) {
                    todayJournalData = response.body()
                } else {
                    // TODO: handle error
                    val statusResponse: StatusResponse = response.body()
                    println(statusResponse.body)
                }
            } catch (e: Exception) {
                // TODO: handle error
                println(e.message)
            }
        }
    }

    Column(
        Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
    ) {
        Button(
            onClick = { appState.pageState.value = PageStates.SETTINGS },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC7E6C9)),
            modifier = Modifier.padding(start = 304.dp, top = 12.dp)
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = "settings",
                modifier = Modifier.size(48.dp),
            )
        }
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier.padding(top = 72.dp)
            ) {
                if (todayJournalData.moods.isNotEmpty()) {
                    WithInfo(today, todayJournalData, appState)
                } else {
                    WithoutInfo()
                }
            }

            Column(
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.padding(start = 64.dp))
                    Box(
                        modifier = Modifier
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                            .size(height = 86.dp, width = 120.dp)
                    ) {
                        Button(
                            onClick = { appState.pageState.value = PageStates.AFFIRMATION },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "☺\uFE0F",
                                    style = TextStyle(
                                        fontSize = 30.sp
                                    )
                                )
                                Text(
                                    text = "Affirmation",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(start = 30.dp))

                    Box(
                        modifier = Modifier
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                            .size(height = 86.dp, width = 120.dp)
                    ) {
                        Button(
                            onClick = { appState.pageState.value = PageStates.JOURNAL_STEP1 },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "\uD83D\uDCA1",
                                    style = TextStyle(
                                        fontSize = 30.sp
                                    )
                                )
                                Text(
                                    text = "Journal",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(end = 64.dp))
                }

                Column(
                    modifier = Modifier.padding(top = 50.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.padding(start = 64.dp))
                        Box(
                            modifier = Modifier
                                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                                .size(height = 86.dp, width = 120.dp)
                        ) {
                            Button(
                                onClick = { appState.pageState.value = PageStates.MEDITATE },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "\uD83E\uDDD8",
                                        style = TextStyle(
                                            fontSize = 30.sp
                                        )
                                    )
                                    Text(
                                        text = "Meditate",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.padding(start = 30.dp))

                        Box(
                            modifier = Modifier
                                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                                .size(height = 86.dp, width = 120.dp)
                        ) {
                            Button(
                                onClick = { appState.pageState.value = PageStates.PHOTOBOOK },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "\uD83D\uDDBC",
                                        style = TextStyle(
                                            fontSize = 30.sp
                                        )
                                    )
                                    Text(
                                        text = "Photobook",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(end = 64.dp))
                        }
                    }
                }
            }
        }
    }
}