package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun WithInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "You're doing great today!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF3D3D3D),

        )
    }

    Column(
        modifier = Modifier
            .padding(top = 50.dp, start = 30.dp, end = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .size(height = 100.dp, width = 140.dp)
            ) {
                Text(
                    text = "Today, Feb 21",
                    style = TextStyle(
                        color = Color(0xFF8FCAB5),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "\nI'm feeling happy!",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun WithoutInfo() {
    Text (
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
fun HomePage(pageState: MutableState<PageStates>) {
    Column(
        Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(top = 128.dp)
        ) {
            // TODO: only show one of these depending on state
             WithInfo()
//            WithoutInfo()
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
                        .padding(start = 30.dp)
                        .size(height = 86.dp, width = 100.dp)
                ) {
                    Button(
                        onClick = { pageState.value = PageStates.AFFIRMATION },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Affirmation",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(start = 30.dp))

                Box(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(start = 20.dp)
                        .size(height = 86.dp, width = 100.dp)
                ) {
                    Button(
                    onClick = { pageState.value = PageStates.JOURNAL_STEP1 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Journal",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
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
                            .padding(start = 20.dp)
                            .size(height = 86.dp, width = 100.dp)
                    ) {
                        Button(
                            onClick = { pageState.value = PageStates.MEDITATE },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Meditate",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(start = 30.dp))

                    Box(
                        modifier = Modifier
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                            .padding(start = 20.dp)
                            .size(height = 86.dp, width = 100.dp)
                    ) {
                        Button(
                            onClick = { pageState.value = PageStates.PHOTOBOOK },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
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

