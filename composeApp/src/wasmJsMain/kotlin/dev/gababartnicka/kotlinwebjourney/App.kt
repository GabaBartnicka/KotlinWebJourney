package dev.gababartnicka.kotlinwebjourney

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

sealed class Screen {
    object TaskList : Screen()
    data class TaskDetail(val taskId: Long) : Screen()
}

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.TaskList) }
        
        when (val screen = currentScreen) {
            is Screen.TaskList -> {
                TaskListScreen(
                    onTaskClick = { taskId ->
                        currentScreen = Screen.TaskDetail(taskId)
                    }
                )
            }
            
            is Screen.TaskDetail -> {
                TaskDetailScreen(
                    taskId = screen.taskId,
                    onNavigateBack = {
                        currentScreen = Screen.TaskList
                    }
                )
            }
        }
    }
}