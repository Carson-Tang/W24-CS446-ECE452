package ca.uwaterloo.cs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.uwaterloo.cs.api.JournalApiService.deleteJournalByUserId
import ca.uwaterloo.cs.api.PhotoApiService.deleteUserPhotos
import ca.uwaterloo.cs.api.UserApiService.deleteUser
import com.an.room.db.JournalDB
import com.an.room.db.PhotoDB
import com.an.room.db.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

suspend fun localClearData(appState: AppState) {
    appState.resetToDefault()
    withContext(Dispatchers.IO) {
        val userDB = UserDB.getDB(appState.context)
        val userDao = userDB.userDao()
        userDao.deleteAll()

        val photoDB = PhotoDB.getDB(appState.context)
        val photoDao = photoDB.photoDao()
        photoDao.deleteAll()

        val journalDB = JournalDB.getDB(appState.context)
        val journalDao = journalDB.journalDao()
        journalDao.deleteAll()
    }
}

suspend fun updateUserPIN(appState: AppState, newPIN: String) {
    withContext(Dispatchers.IO) {
        val userDB = UserDB.getDB(appState.context)
        val userDao = userDB.userDao()
        userDao.updatePINById(newPIN)
    }
}

fun logout(appState: AppState) {
    if (appState.useCloud.value) {
        runBlocking {
            appState.dataStore.setJwt("")
        }
    } else {
        runBlocking {
            localClearData(appState)
        }
    }
}

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

@Composable
fun SettingsPage(appState: AppState) {
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
                /* TODO: add a useNotifications state */
                Switch(
                    appState.useJournalForAffirmations.value,
                    onCheckedChange = {
                        appState.useJournalForAffirmations.value = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF7BB6A1)
                    )
                )
                Switch(
                    appState.useJournalForAffirmations.value,
                    onCheckedChange = {
                        appState.useJournalForAffirmations.value = it
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
                            runBlocking {
                                updateUserPIN(appState, "")
                            }
                            appState.hashedPIN.value = ""
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF7BB6A1)
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 175.dp)
                .size(width = 460.dp, height = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp, 80.dp)
                    .background(color = Color(0xFFA1CDB0), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = {
                        appState.pageState.value = PageStates.DISCLAIMER
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA1CDB0)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Disclaimer",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
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
                    .background(color = Color(0xFF7BB6A1), shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = {
                        appState.pageState.value = PageStates.WELCOME
                        appState.nameState.value = ""
                        logout(appState)
                        appState.setPageHistoryToWelcome()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6A1)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = if (appState.useCloud.value) "Log out" else "Clear data",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                .size(width = 200.dp, height = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton( onClick = {
                runBlocking {
                    // catch errors
                    deleteUser(appState.userId.value, appState.dataStore.getJwt())
                    deleteJournalByUserId(appState.userId.value, appState.dataStore.getJwt())
                    deleteUserPhotos(appState.userId.value, appState.dataStore.getJwt())
                }
            }) {
                Text(
                    text = if (appState.useCloud.value) "Delete account" else "",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF649E8A)
                )
            }
        }

    }
}

