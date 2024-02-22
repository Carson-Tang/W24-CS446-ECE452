package ca.uwaterloo.cs

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
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
  val firstNameState = remember { mutableStateOf("") }
  val lastNameState = remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    OutlinedTextField(
      value = firstNameState.value,
      onValueChange = { firstNameState.value = it },
      label = { Text("First Name") },
      modifier = Modifier.padding(16.dp)
    )

    OutlinedTextField(
      value = lastNameState.value,
      onValueChange = { lastNameState.value = it },
      label = { Text("Last Name") },
      modifier = Modifier.padding(16.dp)
    )

    Button(
      onClick = {
        val firstName = firstNameState.value
        val lastName = lastNameState.value

        if (firstName.isNotBlank() && lastName.isNotBlank()) {
          val user = User(firstName = firstName, lastName = lastName)
          saveUser(user, context)
        }
      },
      modifier = Modifier.padding(16.dp)
    ) {
      Text("Save")
    }
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
