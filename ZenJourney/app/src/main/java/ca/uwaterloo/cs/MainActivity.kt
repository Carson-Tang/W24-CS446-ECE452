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

    Scaffold(
        bottomBar = {
            if (pageState.value !in arrayOf(
                    PageStates.WELCOME,
                    PageStates.LOGIN,
                    PageStates.SIGNUP_STEP1,
                    PageStates.SIGNUP_STEP2
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
                journalEntry
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
    journalEntry: MutableState<String>
) {
    when (pageState.value) {
        PageStates.WELCOME -> WelcomePage(pageState)
        PageStates.LOGIN -> LoginPage(pageState)
        PageStates.SIGNUP_STEP1 -> SignUpPage1(pageState, nameState)
        PageStates.SIGNUP_STEP2 -> SignUpPage2(pageState, nameState)
        PageStates.HOME -> HomePage(pageState)
        PageStates.MEDITATE -> MeditatePage(pageState)
        PageStates.AFFIRMATION -> AffirmationPage(pageState)
        PageStates.PHOTOBOOK -> PhotobookPage(context, pageState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(pageState, selectedDate)
        PageStates.JOURNAL_STEP2 -> JournalPage2(pageState, selectedDate, selectedMoods)
        PageStates.JOURNAL_STEP3 -> JournalPage3(pageState, selectedDate, journalEntry)
        PageStates.PAST_JOURNAL -> PastJournalPage(pageState)
        PageStates.SETTINGS -> SettingsPage(pageState)
    }
}
