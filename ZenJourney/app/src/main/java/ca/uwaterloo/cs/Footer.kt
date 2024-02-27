package ca.uwaterloo.cs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.uwaterloo.cs.PageStates


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Footer(pageState: MutableState<PageStates>) {
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(){
        }
        Column(
            modifier = Modifier
                .size(width = 420.dp, height = 100.dp) // Fixed size
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .background(Color.White),
        ){
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { pageState.value = PageStates.PHOTOBOOK },
                ) {
                    Text("1")
                }
                Button(
                    onClick = { pageState.value = PageStates.AFFIRMATION },
                ) {
                    Text("2")
                }
                Button(
                    onClick = { pageState.value = PageStates.HOME },
                ) {
                    Text("3")
                }
                Button(
                    onClick = { pageState.value = PageStates.JOURNAL_STEP1 },
                ) {
                    Text("4")
                }
                Button(
                    onClick = { pageState.value = PageStates.MEDITATE },
                ) {
                    Text("5")
                }
            }
        }
    }
}
