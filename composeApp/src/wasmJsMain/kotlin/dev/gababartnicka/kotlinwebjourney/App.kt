package dev.gababartnicka.kotlinwebjourney

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun App() {
    MaterialTheme {
        TaskListScreen()
    }
}