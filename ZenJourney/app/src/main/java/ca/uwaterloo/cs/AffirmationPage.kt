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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AffirmationPage(pageState: MutableState<PageStates>) {
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
                    Text(
                        text = "Make way for the\nunprecedented and\nwatch your reality\nrearrange yourself",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF4F4F4F),
                        modifier = Modifier.align(Alignment.Center)
                    )
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
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { /* TODO: change to next affirmation - fetch from API or stored list */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
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

