package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.repository.FavoritesRepository
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.ui.favorites.FavoritesScreen
import com.example.collegeschedule.ui.schedule.ScheduleScreen
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeScheduleTheme {
                CollegeScheduleApp()
            }
        }
    }
}

@Preview
@Composable
fun CollegeScheduleApp() {
    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.HOME)
    }
    var selectedGroupFromFavorites by remember { mutableStateOf<String?>(null) }

    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5227/") // ← Поменял порт на 5227 (ваш API порт)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api = remember { retrofit.create(ScheduleApi::class.java) }
    val repository = remember { ScheduleRepository(api) }

    // Для передачи данных между экранами
    var groupToShow by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) },
                        selected = destination == currentDestination,
                        onClick = {
                            currentDestination = destination
                            // При переходе на HOME, сбрасываем выбранную группу
                            if (destination == AppDestinations.HOME) {
                                groupToShow = null
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentDestination) {
            AppDestinations.HOME -> ScheduleScreen(
                initialGroup = groupToShow, // Передаём группу для автовыбора
                onNavigateToFavorites = {
                    currentDestination = AppDestinations.FAVORITES
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            AppDestinations.FAVORITES -> FavoritesScreen(
                onBackClick = {
                    currentDestination = AppDestinations.HOME
                },
                onGroupSelected = { groupName ->
                    // Сохраняем выбранную группу и переходим к расписанию
                    groupToShow = groupName
                    currentDestination = AppDestinations.HOME
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            AppDestinations.PROFILE -> Text(
                "Профиль студента",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Главная", Icons.Default.Home),
    FAVORITES("Избранное", Icons.Default.Favorite),
    PROFILE("Профиль", Icons.Default.AccountBox),
}