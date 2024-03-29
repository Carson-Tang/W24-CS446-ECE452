package ca.uwaterloo.cs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.uwaterloo.cs.userstrategy.CloudUserStrategy
import ca.uwaterloo.cs.userstrategy.LocalUserStrategy
import org.mindrot.jbcrypt.BCrypt

@Composable
fun SignUpCloud(
    appState: AppState
) {
    fun primaryAction() {
        appState.userStrategy = CloudUserStrategy()
        appState.pageState.value = PageStates.SIGNUP_STEP2
    }

    fun secondaryAction() {
        appState.userStrategy = LocalUserStrategy()
        appState.pageState.value = PageStates.SIGNUP_STEP3
    }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "☁\uFE0F\n\nWould you like us\n to back-up your data?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 40.dp),
        )
        Text(
            "This requires\n the app to use\n internet connections.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp),
        )
        Text(
            "This cannot be\n changed later.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp),
        )
        TextButton(
            modifier = Modifier.padding(bottom = 64.dp),
            onClick = { appState.pageState.value = PageStates.SIGNUP_CLOUD_MORE }) {
            Text("Learn more", style = MaterialTheme.typography.labelSmall)
        }
        CustomizationActionButtons(
            appState,
            ::primaryAction,
            ::secondaryAction
        )
    }
}

@Composable
fun SignUpCloudLearnMore(
    appState: AppState
) {
    fun primaryAction() {
        appState.pageState.value = PageStates.SIGNUP_CLOUD
    }

    fun secondaryAction() {}

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "If you choose for your\n data to be backed-up,\n we will store your data\n in a cloud server. If you\n delete this app, it will be\n saved and restored.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 104.dp, bottom = 40.dp),
        )
        Text(
            "If you do not choose this\n feature, your data will be\n lost once the app is deleted.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 144.dp),
        )
        CustomizationActionButtons(
            appState,
            ::primaryAction,
            ::secondaryAction
        )
    }
}

@Composable
fun SignUpAffirmation(
    appState: AppState
) {
    fun primaryAction() {
        appState.useJournalForAffirmations.value = true
        appState.pageState.value = PageStates.SIGNUP_PIN
    }

    fun secondaryAction() {
        appState.useJournalForAffirmations.value = false
        appState.pageState.value = PageStates.SIGNUP_PIN
    }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "\uD83D\uDD12\n\nWould you like us to\n send you affirmations\n based on the moods\n inputted on your\n journal entries?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 104.dp, bottom = 40.dp),
        )
        Text(
            "This can be changed\n later.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 160.dp),
        )
        CustomizationActionButtons(
            appState,
            ::primaryAction,
            ::secondaryAction
        )
    }
}

@Composable
fun SignUpPIN(appState: AppState) {
    val pinState = remember { mutableStateOf("") }
    val pinErrorState = remember { mutableStateOf(PINErrorStates.NONE) }

    fun primaryAction() {
        if (pinErrorState.value == PINErrorStates.NONE && pinState.value.length == 4) {
            appState.hashedPIN.value = BCrypt.hashpw(pinState.value, BCrypt.gensalt())
            appState.userStrategy!!.storeUserSettings(appState)
            appState.pageState.value = PageStates.HOME
            appState.setPageHistoryToHome()
        } else {
            pinErrorState.value = PINErrorStates.INVALID_PIN_FORMAT
        }
    }

    fun secondaryAction() {
        appState.hashedPIN.value = ""
        appState.userStrategy!!.storeUserSettings(appState)
        appState.pageState.value = PageStates.HOME
        appState.setPageHistoryToHome()
    }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "\uD83D\uDD10\n\nPlease create a\n 4-digit PIN to\n further authenticate \n your account",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 104.dp, bottom = 40.dp),
        )
        PINTextFieldComponent(pinState, pinErrorState)
        Spacer(modifier = Modifier.padding(top = 160.dp))
        CustomizationActionButtons(
            appState,
            ::primaryAction,
            ::secondaryAction
        )
    }
}

@Composable
fun CustomizationActionButtons(
    appState: AppState,
    primaryAction: () -> Unit,
    secondaryAction: () -> Unit,
) {
    val mainActionText =
        if (appState.pageState.value == PageStates.SIGNUP_PIN) "Continue"
        else if (appState.pageState.value == PageStates.SIGNUP_CLOUD_MORE) "Go back"
        else if (arrayOf(
                PageStates.SIGNUP_AFFIRMATION,
                PageStates.SIGNUP_CLOUD
            ).contains(appState.pageState.value)
        ) "Yes"
        else ""
    ElevatedButton(
        onClick = primaryAction,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(width = 184.dp, height = 56.dp),
        colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primaryContainer),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            mainActionText,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
    }
    if (appState.pageState.value != PageStates.SIGNUP_CLOUD_MORE) {
        TextButton(onClick = secondaryAction) {
            Text(
                text = if (appState.pageState.value == PageStates.SIGNUP_CLOUD) "No, continue without"
                else if (arrayOf(PageStates.SIGNUP_AFFIRMATION, PageStates.SIGNUP_PIN).contains(
                        appState.pageState.value
                    )
                ) "Not now, continue without"
                else "",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}