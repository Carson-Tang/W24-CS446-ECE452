package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SignUpPage1(pageState: MutableState<PageStates>, nameState: MutableState<String>) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "☺\uFE0F\n\nNice to meet you!\nWhat's your name?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 104.dp),
        )
        TextFieldComponent(nameState, "My name is...")
        ElevatedButton(
            onClick = {
                if (nameState.value.isNotBlank()) pageState.value = PageStates.SIGNUP_STEP2
            },
            modifier = Modifier
                .padding(top = 224.dp)
                .size(width = 184.dp, height = 56.dp),
            colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primaryContainer),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                "Continue",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun SignUpPage2(pageState: MutableState<PageStates>, nameState: MutableState<String>) {
    SignUpLoginPage(
        pageState = pageState,
        headlineText = "\uD83D\uDC69\u200D\uD83D\uDCBB\n\nAwesome!\nLet's create an account for you, ${nameState.value}",
        buttonText = "Register"
    )
}

@Composable
fun LoginPage(pageState: MutableState<PageStates>) {
    SignUpLoginPage(
        pageState = pageState,
        headlineText = "\uD83D\uDC69\u200D\uD83D\uDCBB\n\nWelcome back\nPlease login",
        buttonText = "Login"
    )
}

@Composable
fun SignUpLoginPage(
    pageState: MutableState<PageStates>,
    headlineText: String,
    buttonText: String
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            headlineText,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 40.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextFieldComponent(emailState, "Email")
            TextFieldComponent(passwordState, "Password", true)
        }
        ElevatedButton(
            /* TODO: Add validation!!! */
            onClick = {
                if (emailState.value.isNotBlank() && passwordState.value.isNotBlank()) pageState.value =
                    PageStates.HOME
            },
            modifier = Modifier
                .padding(top = 208.dp)
                .size(width = 184.dp, height = 56.dp),
            colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primaryContainer),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                buttonText,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}