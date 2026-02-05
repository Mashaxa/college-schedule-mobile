package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.data.repository.FavoritesRepository
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange
@Composable
fun ScheduleScreen(
    initialGroup: String? = null,
    onNavigateToFavorites: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selectedGroup by remember { mutableStateOf(initialGroup) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val favoritesRepository = remember { FavoritesRepository(context) }
    val isCurrentGroupFavorite = remember(selectedGroup) {
        selectedGroup?.let { favoritesRepository.isFavorite(it) } ?: false
    }
    LaunchedEffect(selectedGroup) { //загрузка расписания
        if (selectedGroup != null) {
            loading = true
            error = null
            val (start, end) = getWeekDateRange()
            try {
                schedule = RetrofitInstance.api.getSchedule(selectedGroup!!, start, end)
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(              //строка с выбором группы и кнопкой избранного
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GroupSelector(     //поле
                        selectedGroup = selectedGroup,
                        onGroupSelected = { groupName ->
                            selectedGroup = groupName
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            selectedGroup?.let { groupName ->
                                favoritesRepository.toggleFavorite(groupName)
                            }
                        },
                        enabled = selectedGroup != null
                    ) {
                        Icon(
                            imageVector = if (isCurrentGroupFavorite) {
                                Icons.Filled.Star
                            } else {
                                Icons.Outlined.Star
                            },
                            contentDescription = if (isCurrentGroupFavorite) {
                                "Удалить из избранного"
                            } else {
                                "Добавить в избранное"
                            },
                            tint = if (isCurrentGroupFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(        //кнопка перехода к избранному
            onClick = onNavigateToFavorites,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Star, contentDescription = "Избранное")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Перейти к избранному")
        }

        Spacer(modifier = Modifier.height(16.dp))
        when {        //отображение расписания
            selectedGroup == null -> {
                Text(
                    text = "Выберите группу для отображения расписания",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Загрузка расписания...")
                }
            }
            error != null -> {
                Text(
                    text = "Ошибка: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }
            schedule.isEmpty() -> {
                Text("Нет расписания для группы $selectedGroup")
            }
            else -> {
                ScheduleList(data = schedule)
            }
        }
    }
}