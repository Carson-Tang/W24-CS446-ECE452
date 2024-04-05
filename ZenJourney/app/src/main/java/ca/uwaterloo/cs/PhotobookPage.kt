@file:OptIn(ExperimentalEncodingApi::class)

package ca.uwaterloo.cs

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import photo.PhotoRequest
import java.io.ByteArrayOutputStream
import java.security.spec.KeySpec
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Arrays
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.Cipher.SECRET_KEY
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.ceil


data class PhotobookPhoto(
    val year: Int,
    val month: Int,
    val day: Int,
    val image: Bitmap
)

val currentDate = LocalDate.now()
val currentMonth = currentDate.month.value
val currentYear = currentDate.year
val currentDay = currentDate.dayOfMonth

@Composable
fun ScrollablePhotoList(photoList: List<PhotobookPhoto>) {
    LazyColumn {
        items(photoList) { photo ->
            ImageListItem(photo)
        }
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
@Composable
fun GridItem(photosByMonth: Map<Pair<Int, Int>, List<PhotobookPhoto>>, year: Int, month: Int, idx: Int) {
    photosByMonth[Pair(year, month)]?.get(idx)?.image?.let { it1 ->
        Image(
            bitmap = it1.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .border(2.dp, Color(0xFFF1F1F1))
                .padding(start = 10.dp)
        )
    }
}

@Composable
@OptIn(ExperimentalEncodingApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
fun ScrollablePhotoListWithMonth(appState: AppState, photos: List<PhotobookPhoto>) {
    val photosByMonth = photos.groupBy { it.year to it.month }
    // for each month, year pair overlaps when rendered so we
    // add a delta to the top padding
    var monthDelta = 0
        Box(
            modifier = Modifier
                .padding(top = monthDelta.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(4), content = {
                photosByMonth.forEach() { (date, photos) ->
                    header {
                        TextButton(onClick = {
                            appState.selectedPhotoMonth.value = date.second
                            appState.selectedPhotoYear.value = date.first
                            appState.pageState.value = PageStates.PHOTOBOOK_MONTH
                        }) {
                            Text(
                                text = "${getMonthName(date.second)} ${date.first}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFF5B907D),
                                modifier = Modifier
                                    .padding(all = 5.dp)
                            )
                        }
                    }
                    photosByMonth[Pair(date.first, date.second)]?.size?.let {
                        items(it) { idx ->
                            GridItem(photosByMonth, date.first, date.second, idx)
                        }
                    }
                    monthDelta += 95 * ceil((photosByMonth[Pair(date.first, date.second)]?.size ?: 0) / 4.0).toInt()
                }
                })
        }
}

@Composable
fun ImageListItem(photo: PhotobookPhoto) {
    val date = LocalDate.of(photo.year, photo.month, photo.day)
    Row(
        modifier = Modifier
            .padding(start = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${photo.day} ${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).substring(0, 3).lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
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

fun getMonthName(month: Int): String {
    if (month > 0) {
        val monthNames = DateFormatSymbols().months
        return monthNames[month - 1]
    }
    return ""
}

// bitmap -> base64 string
@OptIn(ExperimentalEncodingApi::class)
fun encodeImage(image: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

// base64 string -> bitmap
@OptIn(ExperimentalEncodingApi::class)
fun decodeImage(decodedByte: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
}

suspend fun refreshPhotoList(appState: AppState) {
    val photos = appState.userStrategy!!.getAllPhotos(appState)
    val photoList: List<PhotobookPhoto>? = try {
        photos?.map { photoRes ->
            val decryptedPhoto =
                appState.userStrategy!!.decryptPhoto(appState, photoRes.photoBase64)
            PhotobookPhoto(
                photoRes.year, photoRes.month, photoRes.day, decodeImage(decryptedPhoto)
            )
        }
    } catch (e: Exception) {
        println("Error in refreshPhotoList")
        println(e.message)
        emptyList()
    }
    appState.photos.clear()
    if (photoList != null) {
        appState.photos.addAll(photoList.reversed())
    }
}


@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun AllPhotosPage(appState: AppState) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        refreshPhotoList(appState)
    }
    val showPhotoErrorDialogue = remember { mutableStateOf(false) }

    /* Permissions popups */
    val showChoiceDialog = remember { mutableStateOf(false) }
    val showPermissionDeniedDialog = remember { mutableStateOf(false) }
    // referenced from: https://google.github.io/accompanist/permissions/
    val cameraPermissions =
        rememberPermissionState(Manifest.permission.CAMERA)

    fun addImageToPhotoState(image: Bitmap) {
        coroutineScope.launch {
            val encryptedPhoto = appState.userStrategy!!.encryptPhoto(appState, encodeImage(image))
            val photoRequest = PhotoRequest(
                appState.userId.value,
                encryptedPhoto,
                currentDate.year,
                currentDate.monthValue,
                currentDate.dayOfMonth
            )
            showPhotoErrorDialogue.value =
                !(appState.userStrategy!!.createPhoto(appState, photoRequest))

            if (!showPhotoErrorDialogue.value) {
                appState.photos.add(
                    0, PhotobookPhoto(
                        currentYear, currentMonth, currentDay, image
                    )
                )
            }
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
    if (showPhotoErrorDialogue.value) {
        AlertDialog(
            onDismissRequest = {
                showPhotoErrorDialogue.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPhotoErrorDialogue.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                ) {
                    Text("OK")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Failed to add photo to the photobook.",
                    fontSize = 16.sp
                )
            })
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 600.dp),
        ) {
            Text(
                text = "Photos",
                color = Color(0xFF5B907D),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                ScrollablePhotoListWithMonth(appState, appState.photos)
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

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PhotobookPage(appState: AppState) {
    val coroutineScope = rememberCoroutineScope()
    val userid = appState.userId.value

    LaunchedEffect(Unit) {
        refreshPhotoList(appState)
    }
    val showPhotoErrorDialogue = remember { mutableStateOf(false) }

    fun addImageToPhotoState(image: Bitmap) {
        coroutineScope.launch {
            val encryptedPhoto = appState.userStrategy!!.encryptPhoto(appState, encodeImage(image))
            val photoRequest = PhotoRequest(
                appState.userId.value,
                encryptedPhoto,
                currentDate.year,
                currentDate.monthValue,
                currentDate.dayOfMonth
            )
            showPhotoErrorDialogue.value =
                !(appState.userStrategy!!.createPhoto(appState, photoRequest))

            if (!showPhotoErrorDialogue.value) {
                appState.photos.add(
                    0, PhotobookPhoto(
                        currentYear, currentMonth, currentDay, image
                    )
                )
            }
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

    /* Permissions popups */
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
    if (showPhotoErrorDialogue.value) {
        AlertDialog(
            onDismissRequest = {
                showPhotoErrorDialogue.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPhotoErrorDialogue.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                ) {
                    Text("OK")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Failed to add photo to the photobook.",
                    fontSize = 16.sp
                )
            })
    }

    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .size(width = 500.dp, height = 600.dp),
        ) {
            Text(
                text = "${getMonthName(appState.selectedPhotoMonth.value)} ${appState.selectedPhotoYear.value}",
                color = Color(0xFF649E8A),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.White, shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    val photosByMonth = appState.photos.groupBy { it.year to it.month }
                    photosByMonth[Pair(appState.selectedPhotoYear.value, appState.selectedPhotoMonth.value)]?.let {
                        ScrollablePhotoList(
                            it
                        )
                    }
                    FloatingActionButton(modifier = Modifier.align(Alignment.BottomEnd),
                        shape = CircleShape,
                        containerColor = Color.DarkGray,
                        onClick = { showChoiceDialog.value = true }) {
                        Icon(
                            Icons.Filled.Add, "Add photobook button"
                        )
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 75.dp),
                        shape = CircleShape,
                        containerColor = Color.DarkGray,
                        onClick = { appState.pageState.value = PageStates.PHOTOBOOK_ALL }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew, "Go back to month view"
                        )
                    }
                }
            }
        }
    }
}

