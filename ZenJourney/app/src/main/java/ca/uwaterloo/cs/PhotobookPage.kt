@file:OptIn(ExperimentalEncodingApi::class)

package ca.uwaterloo.cs

import StatusResponse
import android.Manifest
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.cs.api.PhotoApiService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import photo.ListResponse
import photo.PhotoRequest
import photo.PhotoResponse
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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

// bitmap -> base64 string
fun encodeImage(image: Bitmap): String {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val imageByteArray = stream.toByteArray()
    val str = Base64.encode(imageByteArray)
    return str
}

// base64 string -> bitmap
fun decodeImage(encodedImage: String): Bitmap {
    val decodedByte = Base64.decode(encodedImage)
    val image = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    return image
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PhotobookPage(appState: AppState) {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.month.toString()
    val currentYear = currentDate.year.toString()
    val coroutineScope = rememberCoroutineScope()
    val userid = "65f6591ebe57c2026bcb2300" // Hardcoded test user for now
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    LaunchedEffect(Unit) {
        try {
            val response = PhotoApiService.getAllUserPhotos(userid, appState.dataStore.getJwt())
            if (response.status == HttpStatusCode.OK) {
                val listResponse: ListResponse<PhotoResponse> = response.body()
                val photoList = listResponse.list.map { photoRes ->
                    val date = LocalDate.parse(photoRes.uploadDate, formatter)
                    val datestr = "${date.dayOfMonth} ${
                        capitalize(date.dayOfWeek.toString().take(3))
                    }"
                    PhotobookPhoto(
                        datestr, decodeImage(photoRes.photoBase64)
                    )
                }
                appState.photos.clear()
                appState.photos.addAll(photoList.reversed())
            } else {
                val statusResponse: StatusResponse = response.body()
                println("getting photolist failed")
                println(statusResponse.body)
                // TODO:handle failed image creation
                appState.pageState.value = PageStates.HOME
            }
        } catch (e: Exception) {
            // TODO: handle error
            println("some error occured while fetching photolist")
            println(e)
        }
    }

    fun addImageToPhotoState(image: Bitmap) {
        coroutineScope.launch {
            val photoRequest =
                PhotoRequest(userid, encodeImage(image), currentDate.format(formatter))
            try {
                val response = PhotoApiService.createPhoto(photoRequest, appState.dataStore.getJwt())
                if (response.status != HttpStatusCode.Created) {
                    val statusResponse: StatusResponse = response.body()
                    // TODO: handle failed image creation
                    println(statusResponse.body)
                    appState.pageState.value = PageStates.HOME
                }
            } catch (e: Exception) {
                // TODO: handle error
                println(e.message)
            }
            appState.photos.add(
                0, PhotobookPhoto(
                    "${currentDate.dayOfMonth} ${
                        capitalize(currentDate.dayOfWeek.toString().take(3))
                    }", image
                )
            )
        }
    }

    /*
        referenced from:
        https://developer.android.com/jetpack/compose/libraries
        https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.TakePicturePreview
        https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.GetContent
    */
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
                        appState.context.contentResolver,
                        it
                    )
                )
            )
        }
    }

    val showChoiceDialog = remember { mutableStateOf(false) }
    val showPermissionDeniedDialog = remember { mutableStateOf(false) }
    // referenced from: https://google.github.io/accompanist/permissions/
    val cameraPermissions =
        rememberPermissionState(Manifest.permission.CAMERA)

    if (showChoiceDialog.value) {
        AlertDialog(
            onDismissRequest = { showChoiceDialog.value = false },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    onClick = {
                        if (cameraPermissions.status.isGranted) {
                            getCameraContent.launch(null)
                        } else {
                            showPermissionDeniedDialog.value = true
                        }
                        showChoiceDialog.value = false
                    }) {
                    Text("Take picture")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    onClick = {
                        showChoiceDialog.value = false
                        getAlbumContent.launch("image/*")
                    }) {
                    Text("Select image")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Add today's photo",
                    fontSize = 16.sp
                )
            }
        )
    }
    if (showPermissionDeniedDialog.value) {
        AlertDialog(
            onDismissRequest = {
                cameraPermissions.launchPermissionRequest()
                showPermissionDeniedDialog.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        cameraPermissions.launchPermissionRequest()
                        showPermissionDeniedDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                ) {
                    Text("OK")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Camera permission is required for this feature.",
                    fontSize = 16.sp
                )
            })
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
                    ScrollablePhotoList(appState.photos)
                    FloatingActionButton(modifier = Modifier.align(Alignment.BottomEnd),
                        shape = CircleShape,
                        containerColor = Color.DarkGray,
                        onClick = { showChoiceDialog.value = true }) {
                        Icon(
                            Icons.Filled.Add, "Add photobook button"
                        )
                    }
                }
            }
        }
    }
}

