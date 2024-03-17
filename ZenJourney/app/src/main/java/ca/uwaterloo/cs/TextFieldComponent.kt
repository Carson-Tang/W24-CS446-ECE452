package ca.uwaterloo.cs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldComponent(
    valueState: MutableState<String>,
    placeholderText: String,
    errorState: MutableState<InputErrorStates>,
    isPassword: Boolean = false,
) {
    TextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        textStyle = MaterialTheme.typography.labelMedium,
        placeholder = {
            Text(
                placeholderText,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.size(width = 304.dp, height = 80.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        isError = errorState.value != InputErrorStates.NONE,
        supportingText = {
            var errorText = ""
            if (errorState.value == InputErrorStates.EMPTY_INPUT) {
                errorText = "This field cannot be empty"
            } else if (errorState.value == InputErrorStates.INVALID_EMAIL) {
                errorText = "Incorrect email format"
            } else if (errorState.value == InputErrorStates.INVALID_PASSWORD) {
                errorText = "Password must be at least 6 characters"
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = errorText,
                color = MaterialTheme.colorScheme.error
            )
        },
    )
}
