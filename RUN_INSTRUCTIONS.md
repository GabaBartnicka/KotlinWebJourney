# 🚀 Instrukcje Uruchamiania Aplikacji

## IntelliJ IDEA Run Configurations

Przygotowano 3 konfiguracje uruchamiania w IntelliJ IDEA:

### 1. **Spring Boot Server** 
- **Zadanie:** `server:bootRun`
- **Opis:** Uruchamia backend Spring Boot na porcie 8080
- **URL:** http://localhost:8080

### 2. **Compose Multiplatform Frontend (Development)**
- **Zadanie:** `composeApp:wasmJsBrowserDevelopmentRun` 
- **Opis:** Uruchamia frontend Compose Multiplatform w trybie development
- **URL:** Automatycznie otworzy się przeglądarka

### 3. **Compose Multiplatform Frontend (Production)**
- **Zadanie:** `composeApp:wasmJsBrowserProductionRun`
- **Opis:** Uruchamia frontend Compose Multiplatform w trybie production
- **URL:** Automatycznie otworzy się przeglądarka

### 4. **Build All**
- **Zadanie:** `build`
- **Opis:** Buduje cały projekt (shared, frontend, backend)

## 📋 Jak uruchomić w IntelliJ IDEA

1. Otwórz projekt w IntelliJ IDEA
2. W prawym górnym rogu znajdziesz dropdown z konfiguracjami
3. Wybierz odpowiednią konfigurację i kliknij ▶️ (Run) lub 🐛 (Debug)

## 🖥️ Uruchamianie przez terminal

### Backend (Spring Boot)
```bash
# Uruchom serwer Spring Boot
./gradlew server:bootRun

# Serwer będzie dostępny na: http://localhost:8080
# Endpoint testowy: GET http://localhost:8080/
```

### Frontend (Compose Multiplatform)
```bash
# Uruchom frontend w trybie deweloperskim
./gradlew composeApp:wasmJsBrowserDevelopmentRun

# Lub w trybie produkcyjnym
./gradlew composeApp:wasmJsBrowserProductionRun

# Frontend automatycznie otworzy przeglądarkę
# Tryb development ma hot reload i debugging
```

### Budowanie całego projektu
```bash
# Zbuduj wszystkie moduły
./gradlew build

# Zbuduj tylko backend
./gradlew server:build

# Zbuduj tylko frontend  
./gradlew composeApp:build
```

## 🔧 Testowanie

### Backend Tests
```bash
# Uruchom testy backend-u
./gradlew server:test
```

### Sprawdzenie endpointów
```bash
# Test podstawowego endpointu
curl http://localhost:8080

# Oczekiwana odpowiedź: "Spring Boot: Hello, Java Server!"
```

## 📁 Struktura projektu

```
KotlinWebJourney/
├── composeApp/          # Frontend (Compose Multiplatform)
├── server/              # Backend (Spring Boot + Java)
├── shared/              # Wspólny kod (Kotlin Multiplatform)
└── .idea/runConfigurations/  # Konfiguracje IntelliJ
```

## ⚙️ Porty i URL-e

- **Backend:** http://localhost:8080
- **Frontend Dev Server:** Automatycznie przydzielany port (zwykle 8080 lub wyższy)

## 🐛 Debugging

W IntelliJ IDEA możesz używać debugger-a:
1. Ustaw breakpoint-y w kodzie
2. Uruchom konfigurację w trybie Debug (🐛)
3. Aplikacja zatrzyma się na breakpoint-ach

## 📝 Dodatkowe uwagi

- Backend używa Java 17+ i Spring Boot 3.2.0
- Frontend używa Kotlin/Wasm i Compose Multiplatform
- Logi backend-u są ustawione na poziom INFO
- Hot reload jest dostępny dla frontend-u w trybie deweloperskim