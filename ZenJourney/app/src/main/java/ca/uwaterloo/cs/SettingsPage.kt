package ca.uwaterloo.cs

import android.Manifest
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ca.uwaterloo.cs.notifications.NotificationScheduler
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun DisclaimerPage(appState: AppState) {
    Column(
        modifier = Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Disclaimer",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF3D3D3D),
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .size(width = 350.dp, height = 500.dp)
                .padding(top = 10.dp, bottom = 20.dp)
                .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text =
                    "By using this mental wellness app, you acknowledge and agree to the following:\n" +
                            "\n" +
                            "Data Storage: If you opt to store your data on the cloud, please be aware that your personal information, including photos and journal entries, may be stored securely on third-party servers. While we make every effort to maintain the confidentiality and security of your data, we cannot guarantee absolute protection against unauthorized access or breaches.\n" +
                            "\n" +
                            "Personalized Affirmations: This app may utilize the data provided in your journal entries to generate personalized affirmations and recommendations aimed at enhancing your mental wellness. While these affirmations are intended to provide support and encouragement, they are not a substitute for professional advice or treatment. Please consult with a qualified mental health professional if you have any concerns about your mental health.\n" +
                            "\n" +
                            "Privacy: We are committed to protecting your privacy and will not share your personal data with third parties without your explicit consent.\n" +
                            "\n" +
                            "Disclaimer of Liability: The developers and providers of this app shall not be held liable for any damages, losses, or harm arising from the use of this app, including but not limited to reliance on personalized affirmations, data breaches, or inaccuracies in the generated content.\n" +
                            "\n" +
                            "By using this mental wellness app, you agree to the terms outlined in this disclaimer. If you do not agree with any part of this disclaimer, please refrain from using the app.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF579981)
                )
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
                    .background(color = Color(0xFF649E8A), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = {
                        appState.pageState.value = PageStates.SETTINGS
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF649E8A)),
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

fun onTimeSet(appState: AppState, hour: Int, min: Int) {
    NotificationScheduler.pauseNotification(appState)
    NotificationScheduler.scheduleNotification(appState.context, hour, min)
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun SettingsPage(appState: AppState) {
    val notificationPermissions =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    val showNotificationSettingsDialog = remember { mutableStateOf(false) }
    val showDeleteAccountDialog = remember { mutableStateOf(false) }
    val unsuccessfulDeleteAccountDialog = remember { mutableStateOf(false) }
    val successfulDeleteAccountDialog = remember { mutableStateOf(false) }

    if (showNotificationSettingsDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showNotificationSettingsDialog.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNotificationSettingsDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                ) {
                    Text("OK")
                }
            },
            text = {
                Text(
                    color = Color(0xFF4F4F4F),
                    text = "Please update preferences for notifications in your phone settings.",
                    fontSize = 16.sp
                )
            })
    }

    if (successfulDeleteAccountDialog.value) {
        Dialog(onDismissRequest = { appState.pageState.value = PageStates.WELCOME }) {
            Surface(
                modifier = Modifier.width(280.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "We successfully deleted your account. Goodbye and thank you for using ZenJourney!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (unsuccessfulDeleteAccountDialog.value) {
        Dialog(onDismissRequest = { unsuccessfulDeleteAccountDialog.value = false }) {
            Surface(
                modifier = Modifier.width(280.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "We were unable to delete your account. Please try again later.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDeleteAccountDialog.value) {
        Dialog(onDismissRequest = { showDeleteAccountDialog.value = false }) {
            Surface(
                modifier = Modifier.width(280.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Are you sure?",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = Color.Black
                    )
                    Text(
                        text = "This will permanently delete your account and CANNOT be undone. You will lose all of your data, including saved journals and photos.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            onClick = {
                                val showDialog = appState.userStrategy!!.deleteAccount(appState)
                                showDeleteAccountDialog.value = false
                                successfulDeleteAccountDialog.value = showDialog.first
                                unsuccessfulDeleteAccountDialog.value = showDialog.second
                                appState.userStrategy!!.clearJWT(appState)
                            }
                        ) {
                            Text("Yes")
                        }
                        Spacer(modifier = Modifier.padding(20.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            onClick = { showDeleteAccountDialog.value = false }
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }

    Column(
        Modifier
            .background(color = Color(0xFFC7E6C9))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF3D3D3D),
            modifier = Modifier.padding(top = 30.dp, bottom = 24.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) {
                    Text(
                        customizationTitles[it],
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF3D3D3D)
                    )
                }
            }
            Column {
                Switch(
                    notificationPermissions.status.isGranted,
                    onCheckedChange = {
                        showNotificationSettingsDialog.value = true
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF7BB6A1)
                    )
                )
                Switch(
                    appState.useJournalForAffirmations.value,
                    onCheckedChange = {
                        appState.useJournalForAffirmations.value = it
                        appState.userStrategy!!.storeUserSettings(appState)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF7BB6A1)
                    )
                )
                Switch(
                    appState.hashedPIN.value.isNotEmpty(),
                    onCheckedChange = {
                        if (it) {
                            // enable PIN
                            appState.pageState.value = PageStates.SIGNUP_PIN
                        } else {
                            // disable PIN
                            appState.hashedPIN.value = ""
                            appState.userStrategy!!.storeUserSettings(appState)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF7BB6A1)
                    )
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                bottom = if (notificationPermissions.status.isGranted) 150.dp else 210.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (notificationPermissions.status.isGranted) {
                Button(
                    onClick = {
                        TimePickerDialog(
                            appState.context,
                            { _, hour, min -> onTimeSet(appState, hour, min) },
                            8,
                            0,
                            true,
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA1CDB0)),
                    modifier = Modifier.size(400.dp, 70.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Customize Notification Time",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
            Button(
                onClick = {
                    appState.pageState.value = PageStates.DISCLAIMER
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA1CDB0)),
                modifier = Modifier.size(400.dp, 70.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Disclaimer",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    appState.pageState.value = PageStates.WELCOME
                    appState.nameState.value = ""
                    appState.userStrategy!!.logout(appState)
                    appState.setPageHistoryToWelcome()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                modifier = Modifier.size(400.dp, 70.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = appState.userStrategy!!.logoutLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                .size(width = 200.dp, height = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = {
                showDeleteAccountDialog.value = true
            }) {
                Text(
                    text = appState.userStrategy!!.deleteAccountLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF649E8A)
                )
            }
        }
    }
}