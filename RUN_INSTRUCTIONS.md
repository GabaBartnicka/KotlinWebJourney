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

### 4. **Compose Frontend Hot Reload** ⚡
- **Zadanie:** `composeApp:wasmJsBrowserDevelopmentRun --continuous`
- **Opis:** Frontend z automatyczną rekompilacją przy zmianach
- **URL:** http://localhost:8081

### 5. **Build All**
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

# Frontend z HOT RELOAD (automatyczna rekompilacja) ⚡
./gradlew composeApp:wasmJsBrowserDevelopmentRun --continuous

# Lub w trybie produkcyjnym
./gradlew composeApp:wasmJsBrowserProductionRun

# Frontend automatycznie otworzy przeglądarkę na porcie 8081
# Tryb continuous ma natychmiastowy hot reload przy zmianach
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

## ⚡ Hot Reload - Jak to działa

**Problem z Kotlin/Wasm:** Standardowy hot reload nie działa z WebAssembly. Oto rozwiązania:

### 🥇 Najlepsze rozwiązanie - Development Script

```bash
# Uruchom development server z auto-kompilacją
./dev-server.sh
```

**Co robi:**
1. **Continuous compilation** - Gradle automatycznie rekompiluje przy zmianach
2. **HTTP server** na porcie 8081
3. **Instrukcje** jak zobaczyć zmiany

### Jak używać:
1. Uruchom `./dev-server.sh` w terminalu
2. Otwórz http://localhost:8081 w przeglądarce
3. Edytuj pliki w `composeApp/` lub `shared/`
4. Zapisz plik (Ctrl+S)
5. Poczekaj na "BUILD SUCCESSFUL"
6. **Odśwież przeglądarkę (F5)** 🔄

### 🥈 Alternatywnie - IntelliJ IDEA

**"Compose Frontend Watch"** - tylko kompilacja (musisz ręcznie odświeżać)
**"Compose Frontend Hot Reload"** - webpack dev server (może nie działać)

## ⚙️ Porty i URL-e

- **Backend:** http://localhost:8080
- **Frontend (Hot Reload):** http://localhost:8081 ⚡
- **Frontend (zwykły):** Automatycznie przydzielany port

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