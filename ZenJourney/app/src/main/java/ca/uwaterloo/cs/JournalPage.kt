package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun JournalPage1(pageState: MutableState<PageStates>) {
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
                text = "Pick a date to begin journal entry",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Calendar()


    }
}

@Composable
fun JournalPage2(pageState: MutableState<PageStates>) {
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
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )

            Column(
                modifier = Modifier
                    .padding(top = 520.dp, start = 20.dp, end = 20.dp)
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
fun JournalPage3(pageState: MutableState<PageStates>) {
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
                .padding(top = 48.dp)
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
                .size(width = 500.dp, height = 450.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ) {

            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
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
                        .size(180.dp, 80.dp)
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = { pageState.value = PageStates.JOURNAL_STEP3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                        modifier = Modifier
                            .padding(start = 32.dp, bottom = 20.dp)
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
                        .size(180.dp, 80.dp)
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = { pageState.value = PageStates.PAST_JOURNAL },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                        modifier = Modifier
                            .padding(start = 32.dp, bottom = 20.dp)
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
                    text = "Non editable text ..... (for now)",
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


