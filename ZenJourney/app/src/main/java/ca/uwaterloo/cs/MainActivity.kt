package ca.uwaterloo.cs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.activity.OnBackPressedCallback
import ca.uwaterloo.cs.notifications.NotificationScheduler
import ca.uwaterloo.cs.userstrategy.CloudUserStrategy
import ca.uwaterloo.cs.userstrategy.LocalUserStrategy
import ca.uwaterloo.cs.userstrategy.UserStrategy
import ca.uwaterloo.cs.ui.theme.ZenJourneyTheme
import com.auth0.android.jwt.JWT
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    val appState = AppState(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                appState.backButtonTriggered.value = true
                if (appState.prevPageStates.size > 1) {
                    appState.pageState.value =
                        appState.prevPageStates.removeAt(appState.prevPageStates.size - 1)
                } else if (appState.prevPageStates.size == 1) {
                    appState.pageState.value = appState.prevPageStates.get(0)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        setContent {
            ZenJourneyTheme {
                MainContent(appState)
            }
        }

        // set up notification channel and alerts
        setUpNotificationChannel(appState.context)
    }
}

class AppState(val context: Context) {
    // user id
    val userId = mutableStateOf("")

    // what page user sees
    val pageState = mutableStateOf(PageStates.LOADING)
    val prevPageStates = mutableStateListOf<PageStates>()
    val prevPageState = mutableStateOf(pageState.value)

    // checks if the back button was pressed cus if it is then we change the current page state
    // the page state change triggers the listener
    val backButtonTriggered = mutableStateOf(false)

    // users name
    val nameState = mutableStateOf("")

    //photos
    val photos = mutableStateListOf<PhotobookPhoto>()
    val selectedMonth = mutableStateOf(0)

    // journal
    val selectedDate = mutableStateOf(LocalDate.now())
    val selectedMoods = mutableStateOf(listOf<String>())
    val journalEntry = mutableStateOf("")
    val pastJournalEntry = mutableStateOf("")
    val pastSelectedMoods = mutableStateOf(listOf<String>())
    val pastDate = mutableStateOf(LocalDate.now())
    var currMonthJournals = mutableStateOf(listOf<LocalDate>())

    // meditation
    val selectedTune = mutableStateOf(R.raw.once_in_paris)
    val playingTuneId = mutableStateOf(selectedTune.value)

    // timer time in meditation
    val defaultTimeMs = mutableStateOf(60000L)
    val timeMs = mutableStateOf(defaultTimeMs.value)

    // user settings
    val useJournalForAffirmations = mutableStateOf(false)
    val hashedPIN = mutableStateOf("")
    val isPINRequired = mutableStateOf(false)

    // auth
    val dataStore = AppDataStore(context)

    var userStrategy: UserStrategy? = null

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
        useJournalForAffirmations.value = false
        hashedPIN.value = ""
        isPINRequired.value = false
        userStrategy = null
        currMonthJournals.value = emptyList()
    }

    fun setPageHistoryToHome() {
        prevPageState.value = PageStates.HOME
        prevPageStates.clear()
    }

    fun setPageHistoryToWelcome() {
        prevPageState.value = PageStates.WELCOME
        prevPageStates.clear()
    }
}

suspend fun determineUserType(appState: AppState) {
    val jwt = appState.dataStore.getJwt()
    if (jwt.isNotEmpty() && !JWT(jwt).isExpired(5)) {
        appState.userStrategy = CloudUserStrategy()
    } else {
        appState.userStrategy = LocalUserStrategy()
    }
}

fun setUpNotificationChannel(context: Context) {
    val channel = NotificationChannel("1", "zenJourney", NotificationManager.IMPORTANCE_HIGH)
    channel.description = "Channel to send push notifications to user"
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

@Composable
fun MainContent(appState: AppState) {
    LaunchedEffect(true) {
        determineUserType(appState)
        appState.userStrategy!!.loadUserSettings(appState)
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
    LaunchedEffect(appState.pageState.value) {
        // since page changes on back button, we need to check if back button pressed
        // if it was then we don't want to add the previous page into the page history cus it'll go into a loop
        if (!appState.backButtonTriggered.value) {
            appState.prevPageStates.add(appState.prevPageState.value)
        } else {
            appState.backButtonTriggered.value = false
        }
        appState.prevPageState.value = appState.pageState.value
    }
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
        PageStates.PHOTOBOOK_ALL -> AllPhotosPage(appState)
        PageStates.PHOTOBOOK_MONTH -> PhotobookPage(appState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(appState)
        PageStates.JOURNAL_STEP2 -> JournalPage2(appState)
        PageStates.JOURNAL_STEP3 -> JournalPage3(appState)
        PageStates.PAST_JOURNAL -> PastJournalPage(appState)
        PageStates.SETTINGS -> SettingsPage(appState)
        PageStates.DISCLAIMER -> DisclaimerPage(appState)
        PageStates.LOADING -> LoadingPage(appState)
    }
}
