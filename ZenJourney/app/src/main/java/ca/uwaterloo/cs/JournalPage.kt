package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun JournalPage1(appState: AppState) {
    LaunchedEffect (key1 = true) {
        appState.isEditing.value = false
    }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalPage2(appState: AppState) {
    LaunchedEffect (key1 = true) {
        if (!appState.isEditing.value) {
            appState.currSelectedMoods.value = listOf()
            appState.currSelectedCustomMoods.value = listOf()
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (appState.showAddMoodDialog.value && !appState.isModalBottomSheetVisible.value) {
        CustomMoodDialog(appState = appState)
    }

    if (appState.isModalBottomSheetVisible.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = RectangleShape,
            tonalElevation = 0.dp,
            onDismissRequest = {
                appState.isModalBottomSheetVisible.value = false
                appState.searchText.value = ""
            },
            dragHandle = null,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            windowInsets = WindowInsets(0),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth() // Fill the width of the modal bottom sheet
                    .background(Color.Transparent)
            ){
                ComposeEmojiPickerBottomSheetUI(
                    onEmojiClick = { emoji ->
                        appState.isModalBottomSheetVisible.value = false
                        appState.newMoodEmoji.value = emoji.character
                    },
                    searchText = appState.searchText.value,
                    updateSearchText = { updatedSearchText ->
                        appState.searchText.value = updatedSearchText
                    },
                )
            }

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
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(moodEmojisWithLabels) { (emoji, label) ->
                    val isSelected = emoji in appState.currSelectedMoods.value
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                if (isSelected) Color(0xFF74B49D) else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // Toggle selection
                                if (isSelected) {
                                    appState.currSelectedMoods.value = appState.currSelectedMoods.value - emoji
                                } else {
                                    appState.currSelectedMoods.value = appState.currSelectedMoods.value + emoji
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
                items(appState.currSelectedCustomMoods.value) { customEmotion ->
                    val (label, emoji) = customEmotion.split(",")

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable {
                                appState.currSelectedCustomMoods.value = appState.currSelectedCustomMoods.value - customEmotion
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
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Other", fontSize = 12.sp)
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
                        onClick = {
                            appState.selectedMoods.value = appState.currSelectedMoods.value
                            appState.selectedCustomMoods.value = appState.currSelectedCustomMoods.value
                            appState.currSelectedMoods.value = listOf()
                            appState.currSelectedCustomMoods.value = listOf()
                            appState.pageState.value = PageStates.JOURNAL_STEP3
                                  },
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
    if (appState.showAddMoodDialog.value) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onDismissRequest = { appState.showAddMoodDialog.value = false },
            title = { Text("Add Custom Mood", color = Color(0xFF4F4F4F)) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        colors = TextFieldDefaults.colors(),
                        value = appState.newMoodLabel.value,
                        onValueChange = {
                            appState.newMoodLabel.value = it
                            appState.isNewMoodLabelValid.value = appState.newMoodLabel.value.isNotEmpty()
                        },
                        label = { Text("Mood", color = Color(0xFF4F4F4F)) },
                        isError = !appState.isNewMoodLabelValid.value,
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        ComposeEmojiPickerEmojiUI(
                            emojiCharacter = appState.newMoodEmoji.value,
                            onClick = {
                                appState.isModalBottomSheetVisible.value = true
                            },
                            fontSize = 48.sp,
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                        appState.currSelectedCustomMoods.value = appState.currSelectedCustomMoods.value + (appState.newMoodLabel.value + "," + appState.newMoodEmoji.value)
                        appState.newMoodLabel.value = ""
                        appState.newMoodEmoji.value = "😃"
                        appState.showAddMoodDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    enabled = appState.isNewMoodLabelValid.value
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
    DisposableEffect(Unit) {
        onDispose {
            appState.journalEntry.value = ""
        }
    }

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
                text = "Write about your day",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            OutlinedTextField(
                value = appState.journalEntry.value,
                onValueChange = { newValue ->
                    if (newValue.length <= charLimit) {
                        appState.journalEntry.value = newValue
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                placeholder = { Text("Type something...") },
                singleLine = false,
                shape = RoundedCornerShape(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "${appState.journalEntry.value.length} / $charLimit",
                style = MaterialTheme.typography.bodySmall
            )
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
                horizontalArrangement = Arrangement.SpaceBetween
            )
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
                            val selectedMoodsAndCustomMoods = selectedMoodsInWords + appState.selectedCustomMoods.value
                            coroutineScope.launch {
                                val journalRequest = JournalRequest(
                                    year = appState.selectedDate.value.year,
                                    month = appState.selectedDate.value.monthValue,
                                    day = appState.selectedDate.value.dayOfMonth,
                                    moods = selectedMoodsAndCustomMoods,
                                    content = appState.journalEntry.value,
                                    userId = appState.userId.value // Hardcoded test user for now
                                )
                                try {
                                    if (appState.isEditing.value) {
                                        appState.userStrategy?.updateJournal(appState, journalRequest, appState.journalId.value)
                                        appState.isEditing.value = false
                                    } else {
                                        appState.userStrategy?.createJournal(appState, journalRequest)
                                    }


                                    val journalResponse = appState.userStrategy?.getJournalByDate(
                                        appState = appState,
                                        year = appState.selectedDate.value.year,
                                        month = appState.selectedDate.value.monthValue,
                                        day = appState.selectedDate.value.dayOfMonth,
                                    )

                                    appState.journalEntry.value = ""
                                    appState.selectedMoods.value = listOf("")
                                    appState.selectedCustomMoods.value = listOf("")

                                    if (journalResponse != null) {
                                        appState.pastJournalEntry.value = journalResponse.content
                                        appState.pastSelectedMoods.value = journalResponse.moods
                                        appState.pastDate.value = LocalDate.of(
                                            journalResponse.year,
                                            journalResponse.month,
                                            journalResponse.day
                                        )
                                        appState.pastJournalId.value = journalResponse.id
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
fun PastJournalPage(appState: AppState) {
    DisposableEffect(Unit) {
        appState.editableContent.value = appState.pastJournalEntry.value
        onDispose {
            appState.pastDate.value = LocalDate.now()
            appState.pastJournalEntry.value = ""
            appState.pastJournalId.value = ""
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
            modifier = Modifier.padding(top = 2.dp, start = 16.dp, end = 16.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(appState.pastSelectedMoods.value) { mood ->
                val parts = mood.split(",")
                val (label, emoji) = if (parts.size > 1) {
                    Pair(parts[0], parts[1])
                } else {
                    Pair(mood, wordToEmojiMap[mood] ?: "?")
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = emoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = label, fontSize = 12.sp)
                    }
                }
            }
        }

        Column(
            Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "Write about your day",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .size(width = 500.dp, height = 270.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = appState.pastJournalEntry.value,
                    color = Color.Black,
                )
            }

        }
        Column(
            Modifier
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        // Set the editing context
                        appState.isEditing.value = true

                        val (selectedMoods, customSelectedMoods) = appState.pastSelectedMoods.value.partition { !it.contains(",") }
                        val selectedMoodsInEmoji = selectedMoods.map { word ->
                            wordToEmojiMap[word] ?: "?"
                        }

                        appState.currSelectedMoods.value = selectedMoodsInEmoji
                        appState.currSelectedCustomMoods.value = customSelectedMoods

                        appState.journalEntry.value = appState.pastJournalEntry.value
                        appState.selectedDate.value = appState.pastDate.value
                        appState.journalId.value = appState.pastJournalId.value

                        // Navigate to JournalPage2 for editing
                        appState.pageState.value = PageStates.JOURNAL_STEP2
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { appState.pageState.value = PageStates.HOME },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}


