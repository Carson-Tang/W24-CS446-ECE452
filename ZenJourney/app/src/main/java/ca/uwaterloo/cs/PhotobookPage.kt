package ca.uwaterloo.cs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    fun addImageToPhotoState(image: Bitmap) {
        photoState.add(
            0,
            PhotobookPhoto(
                "${currentDate.dayOfMonth} ${
                    capitalize(currentDate.dayOfWeek.toString().take(3))
                }", image
            )
        )
    }


    val getCameraContent =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            /* TODO: upload bitmap to server and update UI */
            if (it != null) {
                addImageToPhotoState(it)
            }
        }
    val getAlbumContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            addImageToPhotoState(
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        it
                    )
                )
            )
        }
    }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    onClick = {
                        showDialog.value = false
                        getCameraContent.launch(null)
                    }) {
                    Text("Take picture")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    onClick = {
                        showDialog.value = false
                        getAlbumContent.launch("image/*")
                    }) {
                    Text("Select image")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Add today's photo by",
                    fontSize = 16.sp
                )
            }
        )
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        shape = CircleShape,
                        containerColor = Color.DarkGray,
                        onClick = {
                            showDialog.value = true
                        }) {
                        Icon(
                            Icons.Filled.Add, "Add photobook button"
                        )
                    }
                }
            }
        }
    }
}

