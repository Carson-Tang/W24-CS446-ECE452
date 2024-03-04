package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter
import java.time.LocalDate

@Composable
fun JournalPage1(pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>) {
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
            CalendarWithHeader(pageState, selectedDate)
        }
    }
}

@Composable
fun JournalPage2(pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>, selectedMoods: MutableState<List<String>>) {
    val moodEmojisWithLabels = listOf(
        "\uD83D\uDE0A" to "Happy",
        "\uD83D\uDE22" to "Sad",
        "\uD83D\uDE20" to "Angry",
        "\uD83D\uDE31" to "Shocked",
        "\uD83D\uDE1E" to "Disappointed",
        "\uD83D\uDE0D" to "Loved",
        "\uD83E\uDD2F" to "Mind Blown",
        "\uD83D\uDE0E" to "Cool",
        "\uD83E\uDD73" to "Party"
    )

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
                text = selectedDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
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
                    val isSelected = emoji in selectedMoods.value
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(if (isSelected) Color.LightGray else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable {
                                // Toggle selection
                                if (isSelected) {
                                    selectedMoods.value = selectedMoods.value - emoji
                                } else {
                                    selectedMoods.value = selectedMoods.value + emoji
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
                        onClick = { pageState.value = PageStates.JOURNAL_STEP3 },
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
fun JournalPage3(pageState: MutableState<PageStates>, selectedDate: MutableState<LocalDate>, journalEntry: MutableState<String>) {
    val charLimit = 2000

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
                text = selectedDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
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
                    value = journalEntry.value,
                    onValueChange = { newValue ->
                        if (newValue.length <= charLimit) {
                            journalEntry.value = newValue
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
            Text("${journalEntry.value.length} / $charLimit", style = MaterialTheme.typography.bodySmall)
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
                        onClick = { pageState.value = PageStates.JOURNAL_STEP3 },
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
                        onClick = { pageState.value = PageStates.PAST_JOURNAL },
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
fun PastJournalPage(pageState: MutableState<PageStates>) {
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
                text = "February 22, 2024",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF649E8A)
            )
        }

        Column(
            Modifier
                .padding(top = 32.dp)
        ) {
            Text(
                text = "You felt: \n\uD83D\uDE0C\n Relaxed",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Column(
            Modifier
                .padding(top = 32.dp)
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
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "Non editable text .....",
                    color = Color.Black,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 120.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp, 80.dp)
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { pageState.value = PageStates.HOME },
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


