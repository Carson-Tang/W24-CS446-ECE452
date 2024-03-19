package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking

@Composable
fun SettingsPage(appState: AppState) {
    val customizationStates = arrayOf(
        appState.useJournalForAffirmations,
        appState.useJournalForAffirmations,
        appState.usePIN
    )
    Column(
        Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF3D3D3D),
            modifier = Modifier.padding(top = 30.dp, bottom = 24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) {
                    Text(
                        customizationTitles[it],
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF3D3D3D)
                    )
                }
            }
            Column {
                /* TODO: add a useNotifications state */
                repeat(3) { idx ->
                    Switch(
                        customizationStates[idx].value,
                        onCheckedChange = {
                            customizationStates[idx].value = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Color(0xFF7BB6A1)
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 30.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Disclaimer",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF3D3D3D)
            )

            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "Disclaimer text here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF579981)
                )
            }

            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "............................................................................\n" +
                            "............................................................................\n" +
                            "............................................................................\n" +
                            "............................................................................\n",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF579981)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp, 80.dp)
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = {
                        appState.pageState.value = PageStates.WELCOME
                        runBlocking {
                            appState.dataStore.setJwt("")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Log out",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

