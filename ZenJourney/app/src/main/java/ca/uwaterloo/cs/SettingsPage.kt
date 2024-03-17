package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingsPage(appState: AppState) {
    Column(
        Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color(0xFF3D3D3D)
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 10.dp, end = 50.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFF579981)
            )
        }

        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 320.dp),
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
                style = MaterialTheme.typography.headlineSmall,
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
                    onClick = { appState.pageState.value = PageStates.WELCOME },
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

