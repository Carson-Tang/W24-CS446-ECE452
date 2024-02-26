package ca.uwaterloo.cs

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
fun TextFieldComponent(valueState: MutableState<String>, placeholderText: String, isPassword: Boolean = false) {
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
        modifier = Modifier.size(width = 304.dp, height = 64.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}
