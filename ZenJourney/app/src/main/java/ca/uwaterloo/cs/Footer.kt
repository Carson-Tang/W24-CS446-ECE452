package ca.uwaterloo.cs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun Footer(pageState: MutableState<PageStates>) {
    NavigationBar(Modifier.fillMaxWidth()) {
        NavigationBarItem(
            selected = pageState.value == PageStates.PHOTOBOOK,
            onClick = { pageState.value = PageStates.PHOTOBOOK },
            icon = {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = "photo-book",
                    modifier = Modifier.size(32.dp),
                )
            })
        NavigationBarItem(
            selected = pageState.value == PageStates.AFFIRMATION,
            onClick = { pageState.value = PageStates.AFFIRMATION },
            icon = {
                Icon(
                    Icons.Outlined.VolunteerActivism,
                    contentDescription = "affirmations",
                    modifier = Modifier.size(32.dp),
                )
            })
        NavigationBarItem(
            selected = pageState.value == PageStates.HOME,
            onClick = { pageState.value = PageStates.HOME },
            icon = {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "home",
                    modifier = Modifier.size(32.dp),
                )
            })
        NavigationBarItem(
            selected = pageState.value == PageStates.JOURNAL_STEP1,
            onClick = { pageState.value = PageStates.JOURNAL_STEP1 },
            icon = {
                Icon(
                    Icons.Outlined.EditNote,
                    contentDescription = "journal",
                    modifier = Modifier.size(32.dp),
                )
            })
        NavigationBarItem(
            selected = pageState.value == PageStates.MEDITATE,
            onClick = { pageState.value = PageStates.MEDITATE },
            icon = {
                Icon(
                    Icons.Outlined.SelfImprovement,
                    contentDescription = "meditation",
                    modifier = Modifier.size(32.dp),
                )
            })
    }
}
