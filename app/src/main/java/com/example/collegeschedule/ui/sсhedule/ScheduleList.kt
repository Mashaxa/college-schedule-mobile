package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(
    data: List<ScheduleByDateDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        data.forEach { day ->
            item {
                Text(
                    "${day.weekday}, ${day.lessonDate}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (day.lessons.isEmpty()) {
                item {
                    Text(
                        "Нет занятий",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            } else {
                items(day.lessons) { lesson ->
                    PrettyLessonCard(lesson)
                }
            }
        }
    }
}

@Composable
fun PrettyLessonCard(lesson: com.example.collegeschedule.data.dto.LessonDto) {
    val info = lesson.groupParts.values.firstOrNull()

    if (info != null) {
        val cardColor = if (lesson.lessonNumber % 2 == 0)
            Color(0xFFE3F2FD)
        else
            Color(0xFFBBDEFB)

        val buildingColor = when {
            info.building.contains("Главный", true) -> Color(0xFF2196F3)
            info.building.contains("Учебный", true) -> Color(0xFF1976D2)
            else -> MaterialTheme.colorScheme.primary
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = MaterialTheme.shapes.small,
                        color = buildingColor.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                lesson.lessonNumber.toString(),
                                fontWeight = FontWeight.Bold,
                                color = buildingColor
                            )
                        }
                    }

                    Text(
                        lesson.time,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    info.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    info.teacher,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(8.dp),
                        shape = MaterialTheme.shapes.small,
                        color = buildingColor
                    ) {}

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "${info.building}, аудитория ${info.classroom}",
                        color = buildingColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}