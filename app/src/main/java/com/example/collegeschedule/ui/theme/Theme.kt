package com.example.collegeschedule.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
//темная тема
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),       // Светло-голубой (акценты)
    onPrimary = Color(0xFF003153),     // Тёмно-синий на светлом

    secondary = Color(0xFF64B5F6),     // Средний голубой
    onSecondary = Color(0xFF003153),

    tertiary = Color(0xFFB0BEC5),      // Серо-голубой
    onTertiary = Color(0xFF263238),

    background = Color(0xFF121212),    // Тёмный фон
    onBackground = Color(0xFFE0E0E0),  // Светло-серый текст

    surface = Color(0xFF1E1E1E),       // Тёмная поверхность
    onSurface = Color(0xFFE0E0E0),

    surfaceVariant = Color(0xFF2D2D2D), // Вариант поверхности
    onSurfaceVariant = Color(0xFFB0B0B0),

    outline = Color(0xFF424242),       //границы
    outlineVariant = Color(0xFF303030)
)
//светлая
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF64B5F6),
    onSecondary = Color(0xFFFFFFFF),

    // Третичный - нейтральный серый
    tertiary = Color(0xFF757575),
    onTertiary = Color(0xFFFFFFFF),

    // Фоны
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF212121),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),

    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242),

    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFEEEEEE),

    // Дополнительные цвета
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF01579B),

    secondaryContainer = Color(0xFFE1F5FE),
    onSecondaryContainer = Color(0xFF006064),

    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF)
)
@Composable
fun CollegeScheduleTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}