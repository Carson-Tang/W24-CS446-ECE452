package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import journal.JournalRequest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerEmojiUI

@Composable
fun JournalPage1(appState: AppState) {
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(top = 48.dp)

        ) {
            Text(
                text = "\uD83D\uDDD3\uFE0F",
                fontSize = 64.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Pick a date to begin journal entry",
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 24.dp),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }
        Column(
            Modifier
                .padding(top = 20.dp)
        )
        {
            CalendarWithHeader(appState)
        }
    }
}

@Composable
fun JournalPage2(appState: AppState) {
    if (appState.showAddMoodDialog.value) {
        CustomMoodDialog(appState = appState)
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(top = 24.dp)
        ) {
            Text(
                text = appState.selectedDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF649E8A)
            )
        }

        Column(
            Modifier
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(moodEmojisWithLabels) { (emoji, label) ->
                    val isSelected = emoji in appState.selectedMoods.value
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(if (isSelected) Color.LightGray else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable {
                                // Toggle selection
                                if (isSelected) {
                                    appState.selectedMoods.value = appState.selectedMoods.value - emoji
                                } else {
                                    appState.selectedMoods.value = appState.selectedMoods.value + emoji
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = emoji, fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = label, fontSize = 12.sp)
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable {
                                appState.showAddMoodDialog.value = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+", fontSize = 40.sp)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .size(width = 460.dp, height = 75.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = { appState.pageState.value = PageStates.JOURNAL_STEP3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CustomMoodDialog(appState: AppState) {
    var isModalBottomSheetVisible by remember {
        mutableStateOf(false)
    }
    var selectedEmoji by remember {
        mutableStateOf("😃")
    }
    var searchText by remember {
        mutableStateOf("")
    }

    if (appState.showAddMoodDialog.value) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onDismissRequest = { appState.showAddMoodDialog.value = false },
            title = { Text("Add Custom Mood") },
            text = {
                Column {
                    TextField(
                        colors = TextFieldDefaults.colors(),
                        value = appState.newMoodLabel.value,
                        onValueChange = { appState.newMoodLabel.value = it },
                        label = { Text("Mood Text") }
                    )
                    TextField(
                        colors = TextFieldDefaults.colors(),
                        value = appState.newMoodEmoji.value,
                        onValueChange = { appState.newMoodEmoji.value = it },
                        label = { Text("Mood Emoji") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                        appState.selectedMoods.value = appState.selectedMoods.value + (appState.newMoodLabel.value + "," + appState.newMoodEmoji.value)
                        appState.newMoodLabel.value = ""
                        appState.newMoodEmoji.value = ""
                        appState.showAddMoodDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1))
                ) {
                    Text(color = Color(0xFF4F4F4F), text = "Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = { appState.showAddMoodDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1))
                ) {
                    Text(color = Color(0xFF4F4F4F), text = "Cancel")
                }
            }
        )
    }
}

@Composable
fun JournalPage3(appState: AppState) {
    val charLimit = 2000
    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(top = 24.dp)
        ) {
            Text(
                text = appState.selectedDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF649E8A)
            )
        }

        Column(
            Modifier
                .padding(top = 24.dp)
        ) {
            Text(
                text = "What is something you accomplished today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Box(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = appState.journalEntry.value,
                    onValueChange = { newValue ->
                        if (newValue.length <= charLimit) {
                            appState.journalEntry.value = newValue
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    placeholder = { Text("Type something...") },
                    maxLines = 10,
                    singleLine = false,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text("${appState.journalEntry.value.length} / $charLimit", style = MaterialTheme.typography.bodySmall)
        }

        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Box(
                    modifier = Modifier
                        .size(160.dp, 60.dp)
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = { appState.pageState.value = PageStates.JOURNAL_STEP2 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .height(64.dp)
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(start = 16.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp, 80.dp)
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = {
                            val selectedMoodsInWords = appState.selectedMoods.value.map { emoji ->
                                emojiToWordMap[emoji] ?: "Unknown"
                            }
                            coroutineScope.launch {
                                val journalRequest = JournalRequest(
                                    year = appState.selectedDate.value.year,
                                    month = appState.selectedDate.value.monthValue,
                                    day = appState.selectedDate.value.dayOfMonth,
                                    moods = selectedMoodsInWords,
                                    content = appState.journalEntry.value,
                                    userId = appState.userId.value // Hardcoded test user for now
                                )
                                try {
                                    // we can change this flow in the future, but this current creation doesnt return
                                    // the actual journal response, so we just query it again
                                    appState.userStrategy?.createJournal(appState, journalRequest)

                                    val journalResponse = appState.userStrategy?.getJournalByDate(
                                        appState = appState,
                                        year = appState.selectedDate.value.year,
                                        month = appState.selectedDate.value.monthValue,
                                        day = appState.selectedDate.value.dayOfMonth,
                                    )

                                    appState.journalEntry.value = ""
                                    appState.selectedMoods.value = listOf("")

                                    if (journalResponse != null) {
                                        appState.pastJournalEntry.value = journalResponse.content
                                        appState.pastSelectedMoods.value = journalResponse.moods
                                        appState.pastDate.value = LocalDate.of(journalResponse.year, journalResponse.month, journalResponse.day)
                                        appState.pageState.value = PageStates.PAST_JOURNAL
                                    } else {
                                        appState.pageState.value = PageStates.HOME
                                    }
                                } catch (e: Exception) {
                                    // TODO: handle error
                                    println(e.message)
                                }
                            }


                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .height(64.dp)
                    ) {
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PastJournalPage(appState: AppState)
{
    DisposableEffect(Unit) {
        onDispose {
            appState.pastDate.value = LocalDate.now()
            appState.pastSelectedMoods.value = listOf<String>()
            appState.pastJournalEntry.value = ""
        }
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(top = 64.dp)
        ) {
            Text(
                text = appState.pastDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF649E8A)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // need to make this dynamic in future
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(top = 2.dp, start = 16.dp, end = 16.dp, bottom = 2.dp)
        ) {
            items(appState.pastSelectedMoods.value) { mood ->
                val emoji = wordToEmojiMap[mood] ?: "❓"

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = emoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = mood, fontSize = 12.sp)
                    }
                }
            }
        }

        Column(
            Modifier
                .padding(top = 12.dp)
        ) {
            Text(
                text = "What is something you accomplished today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 4.dp, start = 20.dp, end = 20.dp)
                .size(width = 500.dp, height = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = appState.pastJournalEntry.value,
                    color = Color.Black,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp, 80.dp)
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { appState.pageState.value = PageStates.HOME },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

