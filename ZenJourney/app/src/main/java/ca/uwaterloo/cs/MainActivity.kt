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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.activity.OnBackPressedCallback
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
    val appState = AppState(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                appState.backButtonTriggered.value = true
                if (appState.prevPageStates.size > 1) {
                    appState.pageState.value = appState.prevPageStates.removeAt(appState.prevPageStates.size-1)
                } else if (appState.prevPageStates.size == 1) {
                    appState.pageState.value = appState.prevPageStates.get(0)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        setContent {
            ZenJourneyTheme {
                MainContent(this, appState)
            }
        }
    }
}

class AppState(val context: Context) {
    // user id
    val userId = mutableStateOf("")

    // what page user sees
    val pageState = mutableStateOf(PageStates.WELCOME)
    val prevPageStates = mutableStateListOf<PageStates>()
    val prevPageState = mutableStateOf(pageState.value)
    // checks if the back button was pressed cus if it is then we change the current page state
    // the page state change triggers the listener
    val backButtonTriggered = mutableStateOf(false)

    // users name
    val nameState = mutableStateOf("")

    //photos
    val photos = mutableStateListOf<PhotobookPhoto>()

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
    val hashedPIN = mutableStateOf("")
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
        hashedPIN.value = ""
        isPINRequired.value = false
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
                appState.hashedPIN.value = user.pin
                if (appState.hashedPIN.value.isNotEmpty()) {
                    appState.isPINRequired.value = true
                }
                if (!appState.useCloud.value) {
                    appState.pageState.value = PageStates.HOME
                    appState.setPageHistoryToHome()
                }
            }
        }
    }
}

@Composable
fun MainContent(context: Context, appState: AppState) {
    /* TODO: add conditional and logic to retrieve setting when user is cloud */
    LoadLocalUserSettings(context, appState)

    runBlocking {
        val jwt = appState.dataStore.getJwt()
        if (jwt.isNotEmpty() && !JWT(jwt).isExpired(5)) {
            appState.pageState.value = PageStates.HOME
            appState.setPageHistoryToHome()
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
    LaunchedEffect(appState.pageState.value) {
        // since page changes on back button, we need to check if back button pressed
        // if it was then we don't want to add the previous page into the page history cus it'll go into a loop
        println("current page: "+appState.pageState.value)
        println("current list: "+appState.prevPageStates.toList().toString())
        println("back button pressed: "+appState.backButtonTriggered.value)
        if (!appState.backButtonTriggered.value) {
            appState.prevPageStates.add(appState.prevPageState.value)
            println("list AFTER: "+appState.prevPageStates.toList().toString())
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
        PageStates.PHOTOBOOK -> PhotobookPage(appState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(appState)
        PageStates.JOURNAL_STEP2 -> JournalPage2(appState)
        PageStates.JOURNAL_STEP3 -> JournalPage3(appState)
        PageStates.PAST_JOURNAL -> PastJournalPage(appState)
        PageStates.SETTINGS -> SettingsPage(appState)
    }
}
