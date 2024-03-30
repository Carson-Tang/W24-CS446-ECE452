package ca.uwaterloo.cs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun formatTime(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val min = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60

    return String.format("%02d:%02d:%02d", hours, min, sec)
}

@Composable
fun formatTimeTriple(ms: Long): Triple<Int, Int, Int> {
    val hours = TimeUnit.MILLISECONDS.toHours(ms).toInt()
    val min = (TimeUnit.MILLISECONDS.toMinutes(ms) % 60).toInt()
    val sec = (TimeUnit.MILLISECONDS.toSeconds(ms) % 60).toInt()

    return Triple(hours, min, sec)
}

@Composable
fun TimerScreen(appState: AppState) {
    var isRunning by remember {
        mutableStateOf(false)
    }

    var isTimePickerVisible by remember { mutableStateOf(false) }

    Column() {
        if (isTimePickerVisible) {
            TimePickerPopup(appState.defaultTimeMs.value) { hour, minute, second ->
                isTimePickerVisible = false
                appState.defaultTimeMs.value = hour * 3600000L + minute * 60000L + second * 1000L
                appState.timeMs.value = appState.defaultTimeMs.value
            }
        }
    }

    Column(
        modifier = Modifier
            .height(350.dp)
            .padding(bottom = 25.dp),
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
                text = formatTime(appState.timeMs.value),
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
                        isTimePickerVisible = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = null,
                            tint = Color(0xFF8FCAB5)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(24.dp))
                // REFRESH
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        appState.timeMs.value = appState.defaultTimeMs.value
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
                // PLAY
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        if (!isRunning) {
                            isRunning = true
                            mediaPlayer = MediaPlayer.create(appState.context, appState.selectedTune.value)
                            mediaPlayer?.isLooping = true
                            mediaPlayer?.start()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF8FCAB5)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(24.dp))
                // PAUSE
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        isRunning = false
                        mediaPlayer?.pause()
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
            if (appState.timeMs.value == 0L) {
                isRunning = false
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            delay(1000)
            appState.timeMs.value -= 1000
        }
    }
}


@Composable
fun TimePickerPopup(defaultTimeMs: Long, onTimeSelected: (hour: Int, minute: Int, second: Int) -> Unit) {
    var time = formatTimeTriple(defaultTimeMs)
    var hour by remember { mutableStateOf(time.first) }
    var minute by remember { mutableStateOf(time.second) }
    var second by remember { mutableStateOf(time.third) }

    Dialog(onDismissRequest = { /* Dismiss the dialog */ }) {
        Surface(
            modifier = Modifier.width(280.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Meditation Time", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                NumberPicker(value = hour, onValueChange = { hour = it }, label = "Hour")
                NumberPicker(value = minute, onValueChange = { minute = it }, label = "Minute")
                NumberPicker(value = second, onValueChange = { second = it }, label = "Second")

                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                    onClick = {
                        onTimeSelected(hour, minute, second)
                    }
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, label: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.Black,
            )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onValueChange(value - 1) }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = value.toString(),
                color = Color.Black,
                modifier = Modifier.padding(top = 10.dp)
            )
            IconButton(onClick = { onValueChange(value + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}