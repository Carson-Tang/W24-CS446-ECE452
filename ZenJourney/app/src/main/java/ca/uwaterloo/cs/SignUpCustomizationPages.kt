package ca.uwaterloo.cs

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton

@Composable
fun SignUpCloud(pageState: MutableState<PageStates>, useCloud: MutableState<Boolean>, useJournalForAffirmations: MutableState<Boolean>) {
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
            onClick = { pageState.value = PageStates.SIGNUP_CLOUD_MORE }) {
            Text("Learn more", style = MaterialTheme.typography.labelSmall)
        }
        CustomizationActionButtons(pageState, PageStates.SIGNUP_AFFIRMATION, useCloud, useJournalForAffirmations)
    }
}

@Composable
fun SignUpCloudLearnMore(pageState: MutableState<PageStates>, useCloud: MutableState<Boolean>, useJournalForAffirmations: MutableState<Boolean>) {
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
        CustomizationActionButtons(pageState, PageStates.SIGNUP_CLOUD, useCloud, useJournalForAffirmations)
    }
}

@Composable
fun SignUpAffirmation(pageState: MutableState<PageStates>, useCloud: MutableState<Boolean>, useJournalForAffirmations: MutableState<Boolean>) {
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
        CustomizationActionButtons(pageState, PageStates.SIGNUP_PIN, useCloud, useJournalForAffirmations)
    }
}

@Composable
fun SignUpPIN(pageState: MutableState<PageStates>, useCloud: MutableState<Boolean>, useJournalForAffirmations: MutableState<Boolean>) {
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
        /*TODO: add PIN input*/
        CustomizationActionButtons(pageState, PageStates.HOME, useCloud, useJournalForAffirmations)
    }
}

@Composable
fun CustomizationActionButtons(
    pageState: MutableState<PageStates>,
    nextPageState: PageStates,
    useCloud: MutableState<Boolean>,
    useJournalForAffirmations: MutableState<Boolean>
) {
    val mainActionText =
        if (pageState.value == PageStates.SIGNUP_PIN) "Continue"
        else if (pageState.value == PageStates.SIGNUP_CLOUD_MORE) "Go back"
        else if (pageState.value == PageStates.SIGNUP_AFFIRMATION ||
                 pageState.value == PageStates.SIGNUP_CLOUD) "Yes"
        else ""
    ElevatedButton(
        onClick = {
            if (pageState.value == PageStates.SIGNUP_CLOUD) {
                useCloud.value = true
            }
            else if (pageState.value == PageStates.SIGNUP_AFFIRMATION) {
                useJournalForAffirmations.value = true
            }
            pageState.value = nextPageState
        },
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
    if (pageState.value != PageStates.SIGNUP_CLOUD_MORE) {
        TextButton(onClick = {
            pageState.value = nextPageState
            if (pageState.value == PageStates.SIGNUP_CLOUD) {
                useCloud.value = false
            }
            else if (pageState.value == PageStates.SIGNUP_AFFIRMATION) {
                useJournalForAffirmations.value = false
            }
        }) {
            Text(
                text = if (pageState.value == PageStates.SIGNUP_CLOUD) "No, continue without"
                        else if (pageState.value == PageStates.SIGNUP_AFFIRMATION) "Not now, continue without"
                        else "",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}