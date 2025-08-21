package dev.gababartnicka.kotlinwebjourney

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    suspend fun getTask(id: Long): Task? {
        return try {
            client.get("http://localhost:8080/api/tasks/$id").body()
        } catch (e: Exception) {
            println("Error fetching task $id: ${e.message}")
            null
        }
    }

    suspend fun updateTask(task: Task): Task? {
        return try {
            client.put("http://localhost:8080/api/tasks/${task.id}") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }.body()
        } catch (e: Exception) {
            println("Error updating task ${task.id}: ${e.message}")
            null
        }
    }

    suspend fun markTaskDone(id: Long): Task? {
        return try {
            client.patch("http://localhost:8080/api/tasks/$id/done") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error marking task $id as done: ${e.message}")
            null
        }
    }

    fun close() {
        client.close()
    }
}