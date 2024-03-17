package ca.uwaterloo.cs

import StatusResponse
import android.Manifest
import android.util.Patterns.EMAIL_ADDRESS
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.uwaterloo.cs.api.UserApiService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import token.TokenResponse
import user.UserRequest

@Composable
fun SignUpPage1(appState: AppState) {
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
        TextFieldComponent(appState.nameState, "My name is...", errorState)
        ElevatedButton(
            onClick = {
                if (appState.nameState.value.isNotBlank()) appState.pageState.value =
                    PageStates.SIGNUP_CLOUD
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
fun SignUpPage2(
    appState: AppState
) {
    SignUpLoginPage(
        appState = appState,
        headlineText = "\uD83D\uDC69\u200D\uD83D\uDCBB\n\nAwesome!\nLet's create an account for you, ${appState.nameState.value}",
        buttonText = "Register",
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignUpPage3(appState: AppState) {
    // referenced from: https://google.github.io/accompanist/permissions/
    val notificationPermissions =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "\uD83D\uDE0D\n\nHi ${appState.nameState.value}!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 40.dp),
        )
        Text(
            "We are excited\nto begin this\njourney of\nself-care with you.",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 224.dp),
        )
        ElevatedButton(
            onClick = {
                appState.pageState.value = PageStates.SIGNUP_AFFIRMATION
                notificationPermissions.launchPermissionRequest()
            },
            modifier = Modifier.size(width = 184.dp, height = 56.dp),
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
fun LoginPage(
    appState: AppState
) {
    SignUpLoginPage(
        appState = appState,
        headlineText = "\uD83D\uDC69\u200D\uD83D\uDCBB\n\nWelcome back!\nPlease login",
        buttonText = "Login",
    )
}

@Composable
fun SignUpLoginPage(
    appState: AppState,
    headlineText: String,
    buttonText: String,
) {
    val coroutineScope = rememberCoroutineScope()

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

                    coroutineScope.launch {
                        // REGISTER USER
                        if (buttonText == "Register") {
                            val userRequest = UserRequest(
                                name = appState.nameState.value,
                                email = emailState.value.trimEnd(),
                                password = passwordState.value,
                            )
                            try {
                                val response = UserApiService.createUser(userRequest)
                                if (response.status == HttpStatusCode.BadRequest) {
                                    val statusResponse: StatusResponse = response.body()
                                    // TODO: handle with email already exists
                                    println(statusResponse.body)
                                } else if (response.status == HttpStatusCode.Created) {
                                    val tokenResponse: TokenResponse = response.body()
                                    appState.jwt.value = tokenResponse.token
                                    appState.pageState.value = PageStates.SIGNUP_STEP3
                                }
                            } catch (e: Exception) {
                                // TODO: handle error
                                println(e.message)
                            }
                        }
                        // LOGIN USER
                        else {
                            val userRequest = UserRequest(
                                name = appState.nameState.value,
                                email = emailState.value,
                                password = passwordState.value,
                            )
                            try {
                                val response = UserApiService.loginUser(userRequest)
                                if (response.status == HttpStatusCode.BadRequest) {
                                    val statusResponse: StatusResponse = response.body()
                                    // TODO: handle email doesn't exist or invalid password
                                    // probably don't differentiate to keep security
                                    println(statusResponse.body)
                                } else if (response.status == HttpStatusCode.OK) {
                                    val tokenResponse: TokenResponse = response.body()
                                    appState.jwt.value = tokenResponse.token
                                    appState.pageState.value = PageStates.HOME
                                }
                            } catch (e: Exception) {
                                // TODO: handle error
                                println(e.message)
                            }
                        }
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