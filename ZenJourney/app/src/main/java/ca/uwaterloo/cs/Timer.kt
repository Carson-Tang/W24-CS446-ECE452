package ca.uwaterloo.cs

import android.content.Context
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
import android.media.RingtoneManager
import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.ui.window.Dialog

@Composable
fun formatTime(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val min = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60

    return String.format("%02d:%02d:%02d", hours, min, sec)
}

@Composable
fun TimerScreen(context: Context, pageState: MutableState<PageStates>, selectedTune: MutableState<Int>) {
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

    var isTimePickerVisible by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(Triple(0, 0, 0)) }

    // TODO: I don't think the stop actually works
    var mediaPlayer: MediaPlayer? = null;

    Column() {
        if (isTimePickerVisible) {
            TimePickerPopup { hour, minute, second ->
                selectedTime = Triple(hour, minute, second)
                isTimePickerVisible = false
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
    ) {
        Row() {
            Text(
                text = "Add some tunes",
                color = Color(0xFF649E8A),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .clickable(onClick = {pageState.value = PageStates.MEDITATE_PICK_TUNE})
            )
        }
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
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(6.dp))
                ) {
                    IconButton(onClick = {
                        timeMs = defaultTimeMs
                        isRunning = false

                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
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
                        mediaPlayer = MediaPlayer.create(context, selectedTune.value)
                        mediaPlayer?.start()
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
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
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
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            delay(1000)
            timeMs -= 1000
        }
    }
}


@Composable
fun TimePickerPopup(onTimeSelected: (hour: Int, minute: Int, second: Int) -> Unit) {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = { /* Dismiss the dialog */ }) {
        Surface(
            modifier = Modifier.width(280.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Pick a time", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                NumberPicker(value = hour, onValueChange = { hour = it }, label = "Hour")
                NumberPicker(value = minute, onValueChange = { minute = it }, label = "Minute")
                NumberPicker(value = second, onValueChange = { second = it }, label = "Second")

                Spacer(modifier = Modifier.height(16.dp))
                Button(
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
            Text(text = value.toString())
            IconButton(onClick = { onValueChange(value + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}