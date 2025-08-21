package dev.gababartnicka.kotlinwebjourney

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class TaskService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getTasks(): List<Task> {
        return try {
            client.get("http://localhost:8080/api/tasks").body()
        } catch (e: Exception) {
            println("Error fetching tasks: ${e.message}")
            emptyList()
        }
    }

    fun close() {
        client.close()
    }
}