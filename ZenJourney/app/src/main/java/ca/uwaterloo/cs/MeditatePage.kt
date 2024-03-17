package ca.uwaterloo.cs

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MeditatePage(context: Context, appState: AppState) {
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83E\uDDD8\n\nTake some time to\nunwind...",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
            TimerScreen(context, appState)
            ElevatedButton(
                onClick = {
                    appState.pageState.value = PageStates.MEDITATE_PICK_TUNE
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(width = 250.dp, height = 56.dp),
                colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primaryContainer),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "Add some tunes",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Composable
fun MeditatePickTune(context: Context, appState: AppState) {
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = 75.dp, bottom = 10.dp)
        ) {
            Text(
                text = "Pick a relaxing tune",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(width = 365.dp, height = 450.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(top = 30.dp)
            ) {
                ScrollableTunesList(meditationMusic, appState.selectedTune, appState.playingTuneId, context, appState)
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { appState.pageState.value = PageStates.MEDITATE },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}


data class Tune(
    val name: String,
    val tuneId: Int // e.g. R.raw.once_in_paris
)
@Composable
fun TuneListItem(tune: Tune, selectedTune: MutableState<Int>, playingTuneId: MutableState<Int>, context: Context, appState: AppState) {
    val mediaPlayer = remember { MediaPlayer() }
    val tunePlaybackState = remember { mutableStateMapOf<Int, Boolean>() }
    val isCurrPlaying = tunePlaybackState[tune.tuneId] ?: false

    val tint = if (tune.tuneId == selectedTune.value) {
        Color.White
    } else {
        Color(0xFF8FCAB5)
    }

    val backgroundTint = if (tune.tuneId == selectedTune.value) {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFFC8EADE), Color(0xFFA6D6C4))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color.White, Color.White)
        )
    }
    Row(
        modifier = Modifier
            .size(width = 460.dp, height = 75.dp)
            .background(brush = backgroundTint),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = Icons.Outlined.MusicNote,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .padding(start = 30.dp)
                .size(36.dp)
        )
        Text(
            text = tune.name,
            style = TextStyle(
                color = tint,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .clickable(onClick = {selectedTune.value = tune.tuneId})
        )
        Icon(
            imageVector = if (isCurrPlaying) Icons.Outlined.PauseCircle else Icons.Outlined.PlayCircle,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .clickable {
                    tunePlaybackState[tune.tuneId] = !isCurrPlaying
                    if (playingTuneId.value != tune.tuneId) {
                        playingTuneId.value = tune.tuneId
                    }
                    if (!isCurrPlaying) {
                        mediaPlayer.reset()
                        val assetDescriptor: AssetFileDescriptor = context.resources.openRawResourceFd(tune.tuneId)
                        mediaPlayer.setDataSource(assetDescriptor.fileDescriptor, assetDescriptor.startOffset, assetDescriptor.length)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    } else {
                        mediaPlayer.pause()
                    }

                }
                .padding(end = 30.dp),
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            // Release the MediaPlayer when the composable is disposed
            mediaPlayer.release()
        }
    }
}

@Composable
fun ScrollableTunesList(tuneList: List<Tune>, selectedTune: MutableState<Int>, isPickMusicPlaying: MutableState<Int>, context: Context, appState: AppState) {
    LazyColumn {
        items(tuneList) { tune ->
            TuneListItem(tune, selectedTune, isPickMusicPlaying, context, appState)
        }
    }
}
