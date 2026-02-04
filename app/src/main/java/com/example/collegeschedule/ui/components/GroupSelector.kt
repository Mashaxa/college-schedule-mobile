package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import com.example.collegeschedule.data.network.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSelector(
    selectedGroup: String?,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            if (expanded && groups.isEmpty()) { //загрузка групп когда открываем
                coroutineScope.launch {
                    try {
                        groups = RetrofitInstance.api.getAllGroups()
                            .map { it.groupName }
                    } catch (e: Exception) { //пусьой список
                    }
                }
            }
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedGroup ?: searchText,
            onValueChange = {
                searchText = it
                expanded = it.isNotEmpty()
            },
            label = { Text("Группа") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            groups
                .filter { it.contains(searchText, ignoreCase = true) }
                .forEach { groupName ->
                    DropdownMenuItem(   //Dropdown с фильтрацией
                        text = { Text(groupName) },
                        onClick = {
                            onGroupSelected(groupName)
                            expanded = false
                        }
                    )
                }
        }
    }
}