package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun AffirmationPage(appState: AppState) {
    val (currAffirmation, setCurrAffirmation) = remember { mutableStateOf(randAffirmations.random()) }
    val todayMoods = remember { mutableStateListOf<String>() }
    val yestMoods = remember { mutableStateListOf<String>() }
    // we only check today and yesterday's moods for affirmation. if they don't have any
    // then we give them a rand affirmation

    val currentDate = LocalDate.now()
    val yesterday = LocalDate.now().minusDays(1)
    val coroutineScope = rememberCoroutineScope()

    if (appState.useJournalForAffirmations.value) {
        LaunchedEffect(Unit) {
            run {
                coroutineScope.launch {
                    try {
                        val response = appState.userStrategy?.getJournalByDate(
                            appState = appState,
                            day = currentDate.dayOfMonth,
                            month = currentDate.monthValue,
                            year = currentDate.year
                        )
                        // if today not empty
                        if (response != null) {
                            // get a random mood
                            val validMoods = response.moods.filter { customAffirmations.keys.contains(it) }
                            todayMoods.addAll(validMoods)
                            val customAffirmation =
                                customAffirmations[todayMoods.random()]?.random()
                            if (customAffirmation != null) {
                                setCurrAffirmation(customAffirmation)
                            }
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    if (todayMoods.isEmpty()) {
                        // if today empty check yesterday
                        try {
                            val yestResponse = appState.userStrategy?.getJournalByDate(
                                appState = appState,
                                day = yesterday.dayOfMonth,
                                month = yesterday.monthValue,
                                year = yesterday.year
                            )
                            if (yestResponse != null) {
                                // get a random mood
                                val validMoods = yestResponse.moods.filter { customAffirmations.keys.contains(it) }
                                yestMoods.addAll(validMoods)
                                val customAffirmation =
                                    customAffirmations[yestMoods.random()]?.random()
                                if (customAffirmation != null) {
                                    setCurrAffirmation(customAffirmation)
                                }
                            }
                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
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
                modifier = Modifier
                    .padding(all = 20.dp)
                    .size(width = 500.dp, height = 550.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = currAffirmation,
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF4F4F4F),
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .size(width = 460.dp, height = 75.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(400.dp, 80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Button(
                        onClick = {
                            if (!appState.useJournalForAffirmations.value) {
                                setCurrAffirmation(randAffirmations.random())
                            } else {
                                if (todayMoods.isNotEmpty()) {
                                    customAffirmations[todayMoods.random()]?.let {
                                        setCurrAffirmation(
                                            it.random()
                                        )
                                    }
                                } else if (yestMoods.isNotEmpty()) {
                                    customAffirmations[yestMoods.random()]?.let {
                                        setCurrAffirmation(
                                            it.random()
                                        )
                                    }
                                }
                                else {
                                    setCurrAffirmation(randAffirmations.random())
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        modifier = Modifier.fillMaxSize()
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


