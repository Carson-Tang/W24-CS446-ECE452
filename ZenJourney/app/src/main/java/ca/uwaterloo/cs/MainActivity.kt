package ca.uwaterloo.cs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import ca.uwaterloo.cs.ui.theme.ZenJourneyTheme
import com.an.room.db.UserDB
import com.an.room.model.User
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenJourneyTheme {
                MainContent(this)
            }
        }
    }
}

class AppState(val context: Context) {
    // user id
    val userId = mutableStateOf("")

    // what page user sees
    val pageState = mutableStateOf(PageStates.WELCOME)

    // users name
    val nameState = mutableStateOf("")

    // journal
    val selectedDate = mutableStateOf(LocalDate.now())
    val selectedMoods = mutableStateOf(listOf<String>())
    val journalEntry = mutableStateOf("")
    val pastJournalEntry = mutableStateOf("")
    val pastSelectedMoods = mutableStateOf(listOf<String>())
    val pastDate = mutableStateOf(LocalDate.now())

    // meditation
    val selectedTune = mutableStateOf(R.raw.once_in_paris)
    val playingTuneId = mutableStateOf(selectedTune.value)

    // timer time in meditation
    val defaultTimeMs = mutableStateOf(60000L)
    val timeMs = mutableStateOf(defaultTimeMs.value)

    // user settings
    val useCloud = mutableStateOf(false)
    val useJournalForAffirmations = mutableStateOf(false)
    val pin = mutableStateOf("")
    val isPINRequired = mutableStateOf(false)

    // auth
    val dataStore = AppDataStore(context)

    fun resetToDefault() {
        userId.value = ""
        pageState.value = PageStates.WELCOME
        nameState.value = ""
        selectedDate.value = LocalDate.now()
        selectedMoods.value = emptyList()
        journalEntry.value = ""
        pastJournalEntry.value = ""
        pastSelectedMoods.value = emptyList()
        pastDate.value = LocalDate.now()
        selectedTune.value = R.raw.once_in_paris
        playingTuneId.value = selectedTune.value
        defaultTimeMs.value = 60000L
        timeMs.value = defaultTimeMs.value
        useCloud.value = false
        useJournalForAffirmations.value = false
        pin.value = ""
        isPINRequired.value = false
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoadLocalUserSettings(context: Context, appState: AppState) {
    val database = UserDB.getDB(context)
    val userDao = database.userDao()

    val userRes = remember { MutableLiveData<User>() }

    LaunchedEffect(true) {
        val user = withContext(Dispatchers.IO) {
            userDao.getOne().getOrNull(0)
        }
        user?.let {
            userRes.value = it
            withContext(Dispatchers.Main) {
                appState.nameState.value = user.firstName
                appState.useCloud.value = user.useCloud
                appState.useJournalForAffirmations.value = user.useJournalForAffirmations
                appState.pin.value = user.pin
                if (appState.pin.value.isNotEmpty()) {
                    appState.isPINRequired.value = true
                }
                if (!appState.useCloud.value) {
                    appState.pageState.value = PageStates.HOME
                }
            }
        }
    }
}

@Composable
fun MainContent(context: Context) {
    val appState = remember { AppState(context) }

    /* TODO: add conditional and logic to retrieve setting when user is cloud */
    LoadLocalUserSettings(context, appState)

    runBlocking {
        val jwt = appState.dataStore.getJwt()
        if (jwt.isNotEmpty() && !JWT(jwt).isExpired(5)) {
            appState.pageState.value = PageStates.HOME
        }
    }

    Scaffold(
        bottomBar = {
            if (appState.pageState.value !in arrayOf(
                    PageStates.WELCOME,
                    PageStates.LOGIN,
                    PageStates.SIGNUP_STEP1,
                    PageStates.SIGNUP_STEP2,
                    PageStates.SIGNUP_STEP3,
                    PageStates.SIGNUP_CLOUD,
                    PageStates.SIGNUP_CLOUD_MORE,
                    PageStates.SIGNUP_AFFIRMATION,
                    PageStates.SIGNUP_PIN,
                )
            ) {
                Footer(appState)
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PageContent(appState)
        }
    }
}

@Composable
fun PageContent(appState: AppState) {
    when (appState.pageState.value) {
        PageStates.WELCOME -> WelcomePage(appState)
        PageStates.LOGIN -> LoginPage(appState)
        PageStates.SIGNUP_STEP1 -> SignUpPage1(appState)
        PageStates.SIGNUP_STEP2 -> SignUpPage2(appState)
        PageStates.SIGNUP_STEP3 -> SignUpPage3(appState)
        PageStates.SIGNUP_CLOUD -> SignUpCloud(appState)
        PageStates.SIGNUP_CLOUD_MORE -> SignUpCloudLearnMore(appState)
        PageStates.SIGNUP_AFFIRMATION -> SignUpAffirmation(appState)
        PageStates.SIGNUP_PIN -> SignUpPIN(appState)
        PageStates.HOME -> HomePage(appState)
        PageStates.MEDITATE -> MeditatePage(appState)
        PageStates.MEDITATE_PICK_TUNE -> MeditatePickTune(appState)
        PageStates.AFFIRMATION -> AffirmationPage(appState)
        PageStates.PHOTOBOOK -> PhotobookPage(appState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(appState)
        PageStates.JOURNAL_STEP2 -> JournalPage2(appState)
        PageStates.JOURNAL_STEP3 -> JournalPage3(appState)
        PageStates.PAST_JOURNAL -> PastJournalPage(appState)
        PageStates.SETTINGS -> SettingsPage(appState)
    }
}
