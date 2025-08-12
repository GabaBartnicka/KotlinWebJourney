package dev.gababartnicka.kotlinwebjourney

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform