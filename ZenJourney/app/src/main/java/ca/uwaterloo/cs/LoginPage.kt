package ca.uwaterloo.cs

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.an.room.model.User

@Composable
fun LoginPage(context: Context) {
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