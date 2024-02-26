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
  val pageState = remember { mutableStateOf(PageStates.WELCOME) }
  if (pageState.value == PageStates.WELCOME) {
    WelcomePage()
  } else if (pageState.value == PageStates.LOGIN) {
    LoginPage(context)
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
