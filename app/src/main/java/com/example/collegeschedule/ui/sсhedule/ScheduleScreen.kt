package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier
) {
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GroupSelector( //поле
            selectedGroup = selectedGroup,
            onGroupSelected = { groupName ->
                selectedGroup = groupName
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        LaunchedEffect(selectedGroup) {    //загрузка расписания при выборе группы
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
        when { //отображаем результ
            selectedGroup == null -> {
                Text("Выберите группу")
            }
            loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Загрузка...")
                }
            }
            error != null -> {
                Text("Ошибка: $error")
            }
            else -> {
                ScheduleList(data = schedule)
            }
        }
    }
}