package ca.uwaterloo.cs

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable
fun MeditatePage(context: Context, pageState: MutableState<PageStates>, selectedTune: MutableState<Int>) {
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83E\uDDD8\n\nTake some time to\nunwind...",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
        }

        TimerScreen(context, pageState, selectedTune)
    }
}

@Composable
fun MeditatePickTune(pageState: MutableState<PageStates>, selectedTune: MutableState<Int>) {
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 10.dp)
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
                    .size(width = 365.dp, height = 530.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(top = 30.dp)
            ) {
                ScrollableTunesList(listOf(
                    Tune("Once in Paris", R.raw.once_in_paris),
                    Tune("Good Night", R.raw.good_night)
                ), selectedTune)
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = { pageState.value = PageStates.MEDITATE },
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
fun TuneListItem(tune: Tune, selectedTune: MutableState<Int>) {
    val tint = if (tune.tuneId == selectedTune.value) {
        Color(0xFF8FCAB5)
    } else {
        Color.White
    }

    val backgroundTint = if (tune.tuneId == selectedTune.value) {
        Color.White
    } else {
        Color(0xFF99D5BF)
    }
    Row(
        modifier = Modifier
            .size(width = 460.dp, height = 75.dp)
            .background(color = backgroundTint),
        verticalAlignment = Alignment.CenterVertically,
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
                .padding(start = 20.dp)
        )
        // TODO: make this right aligned
        Icon(
            imageVector = Icons.Outlined.PlayCircle,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .clickable(onClick = {selectedTune.value = tune.tuneId}),
        )
    }
}

@Composable
fun ScrollableTunesList(tuneList: List<Tune>, selectedTune: MutableState<Int>) {
    LazyColumn {
        items(tuneList) { tune ->
            TuneListItem(tune, selectedTune)
        }
    }
}
