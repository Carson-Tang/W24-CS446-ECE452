package ca.uwaterloo.cs

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ca.uwaterloo.cs.ui.theme.ZenJourneyTheme
import com.an.room.model.User
import kotlinx.coroutines.launch
import com.an.room.db.UserDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

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
    val pageState = remember { mutableStateOf(PageStates.SETTINGS) }
    val nameState = remember { mutableStateOf("") }

    when (pageState.value) {
        PageStates.WELCOME -> WelcomePage(pageState)
        PageStates.LOGIN -> LoginPage(pageState)
        PageStates.SIGNUP_STEP1 -> SignUpPage1(pageState, nameState)
        PageStates.SIGNUP_STEP2 -> SignUpPage2(pageState, nameState)
        PageStates.HOME -> HomePage(pageState)
        PageStates.MEDITATE -> MeditatePage(pageState)
        PageStates.AFFIRMATION -> AffirmationPage(pageState)
        PageStates.PHOTOBOOK -> PhotobookPage(pageState)
        PageStates.JOURNAL_STEP1 -> JournalPage1(pageState)
        PageStates.JOURNAL_STEP2 -> JournalPage2(pageState)
        PageStates.JOURNAL_STEP3 -> JournalPage3(pageState)
        PageStates.PAST_JOURNAL -> PastJournalPage(pageState)
        PageStates.SETTINGS -> SettingsPage(pageState)
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun saveUser(user: User, context: Context) {
    GlobalScope.launch {
        val userDao = UserDB.getDB(context).userDao()
        userDao.insert(user)
        val allUsers = userDao.getAll()
        Log.e("users", "$allUsers")
    }
}
