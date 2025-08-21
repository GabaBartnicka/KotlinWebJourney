package dev.gababartnicka.kotlinwebjourney

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    onNavigateBack: () -> Unit
) {
    var task by remember { mutableStateOf<Task?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    
    val taskService = remember { TaskService() }
    val scope = rememberCoroutineScope()
    
    DisposableEffect(Unit) {
        onDispose {
            taskService.close()
        }
    }
    
    LaunchedEffect(taskId) {
        isLoading = true
        errorMessage = null
        try {
            task = taskService.getTask(taskId)
            task?.let { editedName = it.name }
        } catch (e: Exception) {
            errorMessage = "Błąd podczas pobierania taska: ${e.message}"
        } finally {
            isLoading = false
        }
    }
    
    fun saveChanges() {
        scope.launch {
            task?.let { currentTask ->
                isLoading = true
                errorMessage = null
                try {
                    val updatedTask = currentTask.copy(name = editedName)
                    val result = taskService.updateTask(updatedTask)
                    if (result != null) {
                        task = result
                        isEditing = false
                    } else {
                        errorMessage = "Nie udało się zapisać zmian"
                    }
                } catch (e: Exception) {
                    errorMessage = "Błąd podczas zapisywania: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }
    
    fun toggleDone() {
        scope.launch {
            task?.let { currentTask ->
                isLoading = true
                errorMessage = null
                try {
                    if (!currentTask.done) {
                        val result = taskService.markTaskDone(currentTask.id)
                        if (result != null) {
                            task = result
                        } else {
                            errorMessage = "Nie udało się oznaczyć jako wykonane"
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = "Błąd podczas zmiany statusu: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Wróć"
                )
            }
            
            Text(
                text = "Szczegóły Taska",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            if (task != null && !isEditing) {
                IconButton(onClick = { 
                    isEditing = true
                    editedName = task?.name ?: ""
                }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edytuj"
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            errorMessage != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            task == null -> {
                Text(
                    text = "Task nie został znaleziony",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            else -> {
                task?.let { currentTask ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Task name (editable)
                            if (isEditing) {
                                OutlinedTextField(
                                    value = editedName,
                                    onValueChange = { editedName = it },
                                    label = { Text("Nazwa taska") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { saveChanges() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            Icons.Default.Save,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Zapisz")
                                    }
                                    
                                    OutlinedButton(
                                        onClick = { 
                                            isEditing = false
                                            editedName = currentTask.name
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Anuluj")
                                    }
                                }
                            } else {
                                Text(
                                    text = currentTask.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Status card
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (currentTask.done) 
                                            MaterialTheme.colorScheme.primaryContainer
                                        else 
                                            MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (currentTask.done) "Wykonany" else "Do zrobienia",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (currentTask.done)
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            else
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                                
                                if (!currentTask.done) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Button(
                                        onClick = { toggleDone() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Oznacz jako wykonane")
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Task metadata
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Informacje o tasku",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "ID: ${currentTask.id}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            currentTask.created?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Utworzony: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            currentTask.updated?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Zaktualizowany: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}