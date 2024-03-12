package ca.uwaterloo.cs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

@Composable
fun formatTime(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val min = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60

    return String.format("%02d:%02d:%02d", hours, min, sec)
}

@Composable
fun TimerScreen() {
    // default timer value
    var defaultTimeMs by remember { // in ms
        mutableStateOf(60000L)
    }

    // what the actual timer shows
    var timeMs by remember {
        mutableStateOf(defaultTimeMs)
    }
    var isRunning by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(bottom = 120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2
                val centerX = size.width / 2
                val centerY = size.height / 2

                drawCircle(
                    color = Color(0xFF70A894),
                    center = Offset(centerX, centerY),
                    radius = radius - 180,
                    style = Stroke(width = 10.dp.toPx())
                )
            }

            Text(
                text = formatTime(timeMs),
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF70A894),
                modifier = Modifier
                    .align(Alignment.Center)
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
            ) {

                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        timeMs = defaultTimeMs
                        isRunning = false
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null,
                            tint = Color(0xFF8FCAB5)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        isRunning = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF8FCAB5)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        isRunning = false
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Pause,
                            contentDescription = null,
                            tint = Color(0xFF8FCAB5)
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(isRunning) {
        while (isRunning) {
            if (timeMs == 0L) {
                isRunning = false
            }
            delay(1000)
            timeMs -= 1000
        }
    }
}
