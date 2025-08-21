package dev.gababartnicka.kotlinwebjourney

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long,
    val name: String,
    val created: String? = null,
    val updated: String? = null,
    val done: Boolean
)