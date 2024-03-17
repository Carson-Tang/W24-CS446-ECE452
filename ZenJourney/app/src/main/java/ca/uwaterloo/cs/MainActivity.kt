package ca.uwaterloo.cs

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ca.uwaterloo.cs.ui.theme.ZenJourneyTheme

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

@Composable
fun MainContent(context: Context) {
    val pageState = remember { mutableStateOf(PageStates.WELCOME) }

    val nameState = remember { mutableStateOf("") }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedMoods = remember { mutableStateOf(listOf<String>()) }

    val journalEntry = remember { mutableStateOf("") }

    val pastJournalEntry = remember { mutableStateOf("") }
    val pastSelectedMoods = remember { mutableStateOf(listOf<String>()) }
    val pastDate = remember { mutableStateOf(LocalDate.now()) }

    val selectedTune = remember {
        mutableStateOf(R.raw.once_in_paris)
    }

    val useCloud = remember { mutableStateOf(false) }
    val useJournalForAffirmations = remember { mutableStateOf(false) }
    val usePIN = remember { mutableStateOf(false) }

    val jwt = remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            if (pageState.value !in arrayOf(
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
                Footer(pageState)
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PageContent(
                context,
                pageState,
                nameState,
                selectedDate,
                selectedMoods,
                journalEntry,
                pastSelectedMoods,
                pastJournalEntry,
                pastDate,
                selectedTune,
                useCloud,
                useJournalForAffirmations,
                usePIN,
                jwt,
            )
        }
    }
}

@Composable
fun PageContent(
    context: Context,
    pageState: MutableState<PageStates>,
    nameState: MutableState<String>,
    selectedDate: MutableState<LocalDate>,
    selectedMoods: MutableState<List<String>>,
    journalEntry: MutableState<String>,
    pastSelectedMoods: MutableState<List<String>>,
    pastJournalEntry: MutableState<String>,
    pastDate: MutableState<LocalDate>,
    selectedTune: MutableState<Int>,
    useCloud: MutableState<Boolean>,
    useJournalForAffirmations: MutableState<Boolean>,
    usePIN: MutableState<Boolean>,
    jwt: MutableState<String>,
) {
    when (pageState.value) {
        PageStates.WELCOME -> WelcomePage(pageState)
        PageStates.LOGIN -> LoginPage(pageState, nameState, jwt)
        PageStates.SIGNUP_STEP1 -> SignUpPage1(pageState, nameState)
        PageStates.SIGNUP_STEP2 -> SignUpPage2(context, pageState, nameState, jwt)
        PageStates.SIGNUP_STEP3 -> SignUpPage3(pageState, nameState)
        PageStates.SIGNUP_CLOUD -> SignUpCloud(pageState, useCloud)
        PageStates.SIGNUP_CLOUD_MORE -> SignUpCloudLearnMore(pageState, useCloud)
        PageStates.SIGNUP_AFFIRMATION -> SignUpAffirmation(pageState, useJournalForAffirmations)
        PageStates.SIGNUP_PIN -> SignUpPIN(pageState, usePIN)
        PageStates.HOME -> HomePage(pageState, jwt)
        PageStates.MEDITATE -> MeditatePage(context, pageState, selectedTune)
        PageStates.MEDITATE_PICK_TUNE -> MeditatePickTune(pageState, selectedTune)
        PageStates.AFFIRMATION -> AffirmationPage(pageState)
        PageStates.PHOTOBOOK -> PhotobookPage(context, pageState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(pageState, selectedDate, pastSelectedMoods, pastJournalEntry, pastDate, jwt)
        PageStates.JOURNAL_STEP2 -> JournalPage2(pageState, selectedDate, selectedMoods)
        PageStates.JOURNAL_STEP3 -> JournalPage3(pageState, selectedDate, journalEntry, selectedMoods, pastSelectedMoods, pastJournalEntry, pastDate, jwt)
        PageStates.PAST_JOURNAL -> PastJournalPage(pageState, pastSelectedMoods, pastJournalEntry, pastDate)
        PageStates.SETTINGS -> SettingsPage(pageState)
    }
}
