package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.repository.FavoritesRepository

@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val favoritesRepository = remember { FavoritesRepository(context) }
    var favoriteGroups by remember { mutableStateOf(favoritesRepository.getFavoritesList()) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Избранное (${favoriteGroups.size})",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteGroups.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Star, "Нет избранного", tint = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Добавьте группы в избранное")
            }
        } else {
            LazyColumn {  //список избранных групп
                items(favoriteGroups) { groupName ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        onClick = { onGroupSelected(groupName) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, "Избранное", tint = Color.Yellow)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(groupName)
                            }
                            IconButton(
                                onClick = {
                                    favoritesRepository.removeFavorite(groupName)
                                    favoriteGroups = favoritesRepository.getFavoritesList()
                                }
                            ) {
                                Icon(Icons.Filled.Delete, "Удалить")
                            }
                        }
                    }
                }
            }
        }
    }
}