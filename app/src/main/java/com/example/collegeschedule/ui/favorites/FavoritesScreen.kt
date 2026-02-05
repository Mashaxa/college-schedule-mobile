package com.example.collegeschedule.ui.favorites

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.repository.FavoritesRepository
import androidx.compose.ui.platform.LocalContext
import com.example.collegeschedule.ui.theme.Blue100
import com.example.collegeschedule.ui.theme.Blue200
import kotlinx.coroutines.delay

@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoritesRepository = FavoritesRepository(LocalContext.current)
    var favoriteGroups by remember { mutableStateOf(favoritesRepository.getFavoritesList()) }
    var deletingGroup by remember { mutableStateOf<String?>(null) }

    // Простая анимация: удаляем группу через 200ms после нажатия
    LaunchedEffect(deletingGroup) {
        if (deletingGroup != null) {
            delay(200) // Короткая задержка для анимации
            favoritesRepository.removeFavorite(deletingGroup!!)
            favoriteGroups = favoritesRepository.getFavoritesList()
            deletingGroup = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок с кнопкой назад
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, "Назад")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Избранное",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${favoriteGroups.size}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteGroups.isEmpty()) {
            // Красивый пустой экран
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Star,
                        "Пусто",
                        modifier = Modifier.size(80.dp),
                        tint = Blue200
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Нет избранных групп",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Добавляйте группы звёздочкой ★",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            // Список с чередованием цветов и анимацией
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteGroups.sorted()) { groupName ->
                    // Показываем карточку только если она не удаляется
                    if (deletingGroup != groupName) {
                        // Чередование цветов для красоты
                        val index = favoriteGroups.indexOf(groupName)
                        val cardColor = if (index % 2 == 0) Blue100 else Blue200

                        // Простая анимация: плавное появление/исчезновение
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = cardColor
                                ),
                                onClick = { onGroupSelected(groupName) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Звезда с простой анимацией при наведении
                                    Icon(
                                        Icons.Filled.Star,
                                        "Избранное",
                                        modifier = Modifier.padding(end = 12.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Text(
                                        groupName,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            deletingGroup = groupName
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Close,
                                            "Удалить",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}