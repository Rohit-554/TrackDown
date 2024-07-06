package io.jadu.trackdown.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TrendyPrimary,
    secondary = TrendySecondary,
    background = TrendyBackgroundDark,
    surface = TrendySurfaceDark,
    onPrimary = TrendyOnPrimary,
    onSecondary = TrendyOnSecondary,
    onBackground = TrendyOnBackgroundDark,
    onSurface = TrendyOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = TrendyPrimary,
    secondary = TrendySecondary,
    background = TrendyBackgroundLight,
    surface = TrendySurfaceLight,
    onPrimary = TrendyOnPrimary,
    onSecondary = TrendyOnSecondary,
    onBackground = TrendyOnBackgroundLight,
    onSurface = TrendyOnSurfaceLight
)

@Composable
fun TrackDownTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Set text colors based on the theme
    val typography = Typography.copy(
        bodyLarge = Typography.bodyLarge.copy(color = colorScheme.onBackground),
        titleLarge = Typography.titleLarge.copy(color = colorScheme.onBackground),
        labelSmall = Typography.labelSmall.copy(color = colorScheme.onBackground)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
