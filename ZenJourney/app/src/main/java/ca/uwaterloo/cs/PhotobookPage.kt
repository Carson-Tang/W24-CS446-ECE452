package ca.uwaterloo.cs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import java.time.LocalDate

data class PhotobookPhoto(
    val date: String,
    val image: Bitmap
)

@Composable
fun ScrollablePhotoList(photoList: List<PhotobookPhoto>) {
    LazyColumn {
        items(photoList) { photo ->
            ImageListItem(photo)
        }
    }
}

@Composable
fun ImageListItem(photo: PhotobookPhoto) {
    Row(
        modifier = Modifier
            .padding(start = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = photo.date,
            color = Color(0xFF4F4F4F),
            style = MaterialTheme.typography.headlineSmall,
        )
        Image(
            bitmap = photo.image.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .padding(start = 20.dp)
        )
    }
}

fun capitalize(s: String): String {
    return s.substring(0, 1).uppercase() + s.substring(1).lowercase();
}

@Composable
fun PhotobookPage(context: Context, pageState: MutableState<PageStates>) {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.month.toString()
    val currentYear = currentDate.year.toString()
    /* TODO: remove hardcoded list */
    val photoState = remember {
        mutableStateListOf(
            PhotobookPhoto(
                "21 Wed",
                BitmapFactory.decodeResource(context.resources, R.drawable.tonton_1)
            ),
            PhotobookPhoto(
                "20 Tue",
                BitmapFactory.decodeResource(context.resources, R.drawable.totoro_2)
            ),
            PhotobookPhoto(
                "19 Mon",
                BitmapFactory.decodeResource(context.resources, R.drawable.pikachu_3)
            )
        )
    }
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            /* TODO: upload bitmap to server and update UI */
            if (it != null) {
                photoState.add(0, PhotobookPhoto("22 Thu", it))
            }
        }

    fun openCamera() {
        getContent.launch(null)
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { openCamera() }) {
            Text("+")
        }
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 620.dp),
        ) {
            Text(
                text = "${capitalize(currentMonth)} $currentYear",
                color = Color(0xFF649E8A),
                style = MaterialTheme.typography.headlineLarge,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(top = 20.dp)
                ) {
                    ScrollablePhotoList(photoState)
                }
            }
        }
    }
}

