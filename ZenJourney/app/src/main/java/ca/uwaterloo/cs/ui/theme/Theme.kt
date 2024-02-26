package ca.uwaterloo.cs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ZenJourneyTheme(content: @Composable () -> Unit) {
    val darkColors = darkColorScheme(
        background = Color.VeryDarkGray,
        surface = Color.VeryDarkGray,
        primary = Color.VeryDarkGray,
    )
    val lightColors = lightColorScheme(
        background = Color(0xFF99D5BF),
        primaryContainer = Color.White,
        onPrimaryContainer = Color(0xFF99D5BF),
        onBackground = Color.White,
        onSurface = Color(0xFF99D5BF),
    )
    val typography = Typography(
        headlineLarge = androidx.compose.ui.text.TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = lightColors.onBackground,
        ),
        headlineMedium = androidx.compose.ui.text.TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = lightColors.onBackground
        ),
        headlineSmall = androidx.compose.ui.text.TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = lightColors.onBackground
        ),
    )
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
        content = {
            val defaultTextStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
            CompositionLocalProvider(LocalTextStyle provides defaultTextStyle) {
                content()
            }
        },
        typography = typography
    )
}

val Color.Companion.VeryDarkGray get() = Color(0xFF121212)
