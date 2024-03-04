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
import android.util.Patterns.EMAIL_ADDRESS

@Composable
fun SignUpPage1(pageState: MutableState<PageStates>, nameState: MutableState<String>) {
    val errorState = remember { mutableStateOf(InputErrorStates.NONE) }
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
        TextFieldComponent(nameState, "My name is...", errorState)
        ElevatedButton(
            onClick = {
                if (nameState.value.isNotBlank()) pageState.value = PageStates.SIGNUP_STEP2
                else {
                    errorState.value = InputErrorStates.EMPTY_INPUT
                }
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
    val emailErrorState = remember { mutableStateOf(InputErrorStates.NONE) }
    val passwordErrorState = remember { mutableStateOf(InputErrorStates.NONE) }
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
            TextFieldComponent(emailState, "Email", emailErrorState)
            TextFieldComponent(passwordState, "Password", passwordErrorState, true)
        }
        ElevatedButton(
            /* TODO: Still need to check with BE */
            onClick = {
                if (emailState.value.isNotBlank() && passwordState.value.isNotBlank()) {
                    // check if email and password is valid (InputErrorStates.NONE)
                    emailErrorState.value =
                        if (!EMAIL_ADDRESS.matcher(emailState.value)
                                .matches()
                        ) InputErrorStates.INVALID_EMAIL else InputErrorStates.NONE

                    passwordErrorState.value =
                        if (passwordState.value.length < 6) InputErrorStates.INVALID_PASSWORD
                        else InputErrorStates.NONE

                    // if both valid, switch to home page
                    if (emailErrorState.value == InputErrorStates.NONE && passwordErrorState.value == InputErrorStates.NONE) {
                        pageState.value = PageStates.HOME
                    }
                } else if (emailState.value.isBlank()) {
                    emailErrorState.value = InputErrorStates.EMPTY_INPUT
                } else if (passwordState.value.isBlank()) {
                    passwordErrorState.value = InputErrorStates.EMPTY_INPUT
                }
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