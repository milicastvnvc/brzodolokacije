package codeverse.brzodolokacije.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = Purple700,
    secondary = secondaryColor,
    background = backgroundColor
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = Purple700,
    secondary = secondaryColor,
    background = lightBackgroundColor

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BrzoDoLokacijeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


private val LightThemeColors = lightColors(
    primary = primaryColor,
    secondary = secondaryColor,
    background = Color.White,
    surface = Color.Gray
)

private val DarkThemeColors = darkColors(
    primary = secondaryColor,
    secondary = primaryColor,
    background = Color.Black,
    surface = Color.DarkGray
)

@Composable
fun McComposeTheme(
    lightTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (lightTheme) LightThemeColors else DarkThemeColors,
        //typography = typography,
        shapes = Shapes,
        content = content
    )
}
