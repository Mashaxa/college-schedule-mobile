package com.example.collegeschedule.ui.theme

import androidx.compose.ui.graphics.Color

val Blue50 = Color(0xFFE3F2FD)
val Blue100 = Color(0xFFBBDEFB)
val Blue200 = Color(0xFF90CAF9)
val Blue300 = Color(0xFF64B5F6)
val Blue400 = Color(0xFF42A5F5)
val Blue500 = Color(0xFF2196F3)
val Blue600 = Color(0xFF1E88E5)
val Blue700 = Color(0xFF1976D2)
val Blue800 = Color(0xFF1565C0)
val Blue900 = Color(0xFF0D47A1)

val Gray50 = Color(0xFFFAFAFA)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFEEEEEE)
val Gray300 = Color(0xFFE0E0E0)
val Gray400 = Color(0xFFBDBDBD)
val Gray500 = Color(0xFF9E9E9E)
val Gray600 = Color(0xFF757575)
val Gray700 = Color(0xFF616161)
val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF212121)

val PureWhite = Color(0xFFFFFFFF)
val PureBlack = Color(0xFF000000)

val SuccessBlue = Color(0xFF4FC3F7)
val WarningBlue = Color(0xFF29B6F6)
val ErrorRed = Color(0xFFF44336)
val InfoCyan = Color(0xFF00BCD4)

val BuildingColors = listOf(
    Blue50,
    Blue100,
    Blue200,
    Blue300,
    Gray100,
    Blue50,
    Gray200,
    Blue100,
    Gray50,
    Blue200
)
fun getBuildingColor(buildingName: String): Color {
    val buildingColorsMap = mapOf(
        "Главный" to Blue100,
        "Учебный" to Blue200,
        "Лабораторный" to Blue300,
        "Спортивный" to Gray100,
        "Библиотечный" to Blue50,
        "Институт" to Blue100,
        "Физический" to Gray200,
        "Корпус инженерии" to Blue200,
        "Корпус Экономики" to Gray50,
        "Корпус Психологии" to Blue300
    )
    return buildingColorsMap.entries.firstOrNull {
        buildingName.contains(it.key, ignoreCase = true)
    }?.value ?: Blue100
}