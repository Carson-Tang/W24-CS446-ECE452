package ca.uwaterloo.cs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PINTextFieldComponent(
    pinInput: MutableState<String>, pinErrorState: MutableState<PINErrorStates>,
    isDialog: Boolean = false
) {
    TextField(
        pinInput.value,
        onValueChange = {
            if (it.all { c -> c.isDigit() }) {
                pinInput.value = it
            }
            pinErrorState.value =
                if (pinInput.value.length != 4) PINErrorStates.INVALID_PIN_FORMAT else PINErrorStates.NONE
        },
        isError = pinErrorState.value != PINErrorStates.NONE,
        supportingText = {
            if (pinErrorState.value == PINErrorStates.INVALID_PIN_FORMAT) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "The PIN must be exactly 4 digits",
                    color = MaterialTheme.colorScheme.error
                )
            } else if (pinErrorState.value == PINErrorStates.INCORRECT_PIN) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Incorrect PIN, please try again",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = if (isDialog) TextFieldDefaults.colors() else TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}