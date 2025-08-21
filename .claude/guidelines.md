# Claude Code Guidelines & Best Practices for Kotlin Compose Multiplatform Web + Spring Boot

## Project Setup & Structure

### Full-Stack Architecture
- **Frontend**: Kotlin Compose Multiplatform Web (WasmJS/JS)
- **Backend**: Java Spring Boot (REST API/WebSocket)
- **Shared Module**: Common DTOs and validation logic in Kotlin

### Recommended Project Structure
```
project-root/
├── frontend/                 # Compose Web module
│   ├── src/
│   │   ├── commonMain/      # Shared frontend code
│   │   ├── wasmJsMain/      # Web-specific code
│   │   └── jsMain/          # JS-specific code (if using JS target)
│   └── build.gradle.kts
├── backend/                  # Spring Boot module
│   ├── src/
│   │   └── main/
│   │       ├── java/       # Spring Boot Java code
│   │       └── resources/
│   │           └── application.yml
│   └── build.gradle.kts
├── shared/                   # Common DTOs (Kotlin)
│   ├── src/
│   │   └── commonMain/
│   └── build.gradle.kts
├── gradle/
└── settings.gradle.kts
```

## IntelliJ IDEA Run Configurations

### Critical Setup for Development
**Always configure proper Run Configurations in IntelliJ IDEA:**

### Frontend Run Configuration
```xml
Name: Frontend Dev Server
Type: Gradle
Tasks: :frontend:wasmJsBrowserDevelopmentRun
VM Options: -Xmx2048m
Environment Variables: 
  - BROWSER=chrome
  - API_URL=http://localhost:8080
```

### Backend Run Configuration
```xml
Name: Spring Boot Backend
Type: Spring Boot
Main Class: com.example.Application
Module: backend.main
Environment Variables:
  - SPRING_PROFILES_ACTIVE=dev
  - SERVER_PORT=8080
VM Options: -Xmx1024m -Dspring.devtools.restart.enabled=true
```

### Combined Full-Stack Configuration
```xml
Name: Full Stack Dev
Type: Compound
Components:
  1. Spring Boot Backend
  2. Frontend Dev Server (with 5s delay)
```

### Important Run Configuration Tips
- **Enable Parallel Run**: Allow multiple instances for different profiles
- **Before Launch Tasks**: Add `gradle clean build` for backend
- **Working Directory**: Set to project root for both configurations
- **Hot Reload**: Enable Spring DevTools and Webpack HMR
- **Port Configuration**: Ensure no port conflicts (8080 for backend, 8081 for frontend dev)

## Spring Boot Backend Configuration

### Essential Dependencies (build.gradle.kts)
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-devtools")
    
    // Kotlin support for shared DTOs
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

### CORS Configuration for Development
```java
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:8081") // Frontend dev server
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

### Application Properties (application.yml)
```yaml
spring:
  profiles:
    active: dev
  
  datasource:
    url: jdbc:postgresql://localhost:5432/myapp
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:password}
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080
  error:
    include-message: always

logging:
  level:
    root: INFO
    com.example: DEBUG
    org.springframework.web: DEBUG
```

## Frontend-Backend Integration

### API Client Setup (Compose Web)
```kotlin
// frontend/src/commonMain/kotlin/api/ApiClient.kt
class ApiClient(private val baseUrl: String = "http://localhost:8080") {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
    
    suspend fun fetchData(): List<DataDto> {
        return client.get("$baseUrl/api/data").body()
    }
}
```

### Spring Boot REST Controller
```java
@RestController
@RequestMapping("/api")
public class DataController {
    
    @Autowired
    private DataService dataService;
    
    @GetMapping("/data")
    public ResponseEntity<List<DataDto>> getData() {
        return ResponseEntity.ok(dataService.getAllData());
    }
    
    @PostMapping("/data")
    public ResponseEntity<DataDto> createData(@Valid @RequestBody DataDto dto) {
        return ResponseEntity.ok(dataService.create(dto));
    }
}
```

### Shared DTOs (Kotlin Multiplatform)
```kotlin
// shared/src/commonMain/kotlin/dto/DataDto.kt
@Serializable
data class DataDto(
    val id: Long? = null,
    val name: String,
    val description: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)
```

## Development Workflow

### Step-by-Step Development Process
1. **Start Database**: `docker-compose up -d postgres`
2. **Run Backend**: Use Spring Boot run configuration in IntelliJ
3. **Wait for Backend**: Ensure backend is fully started (check logs)
4. **Run Frontend**: Use Gradle run configuration for Compose Web
5. **Open Browser**: Navigate to `http://localhost:8081`

### Hot Reload Setup
- **Backend**: Spring DevTools auto-restarts on class changes
- **Frontend**: Webpack Dev Server with HMR enabled
- **Shared Module**: Rebuild required (configure auto-build in IntelliJ)

## WebSocket Integration

### Spring Boot WebSocket Configuration
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:8081")
            .withSockJS();
    }
}
```

### Compose Web WebSocket Client
```kotlin
class WebSocketClient {
    private val client = HttpClient {
        install(WebSockets)
    }
    
    suspend fun connect() {
        client.webSocket(
            method = HttpMethod.Get,
            host = "localhost",
            port = 8080,
            path = "/ws"
        ) {
            // Handle messages
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        // Process message
                    }
                }
            }
        }
    }
}
```

## Testing Strategy

### Backend Testing
```java
@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetData() throws Exception {
        mockMvc.perform(get("/api/data"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
```

### Frontend Testing
```kotlin
@Test
fun testApiClient() = runTest {
    val client = ApiClient("http://localhost:8080")
    val data = client.fetchData()
    assertTrue(data.isNotEmpty())
}
```

### Integration Testing
- Use Testcontainers for database
- MockWebServer for API mocking
- Selenium/Playwright for E2E tests

## Security Best Practices

### Spring Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Configure properly for production
            .cors().and()
            .authorizeHttpRequests()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/**").authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
```

### JWT Authentication Flow
1. Frontend sends credentials to `/api/auth/login`
2. Backend validates and returns JWT token
3. Frontend stores token in memory (not localStorage)
4. Include token in Authorization header for API calls

## Deployment Configuration

### Docker Compose for Development
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: myapp
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
  
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker
  
  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

### Production Build
```bash
# Backend
./gradlew :backend:bootJar

# Frontend
./gradlew :frontend:wasmJsBrowserProductionWebpack
```

## Claude Code Specific Tips for Full-Stack

### Effective Prompting for Full-Stack Development
1. **Specify Both Sides**: "Create a REST endpoint in Spring Boot and corresponding Kotlin Compose Web client"
2. **Include Run Configuration**: "Set up IntelliJ run configurations for Spring Boot backend and Compose Web frontend"
3. **Mention Integration**: "Implement CORS-enabled Spring Boot controller with Ktor client in Compose Web"
4. **Database Context**: "Create JPA entity with PostgreSQL and display data in Compose Web table"

### Common Full-Stack Requests
```
"Create Spring Boot REST API with pagination and Compose Web infinite scroll"
"Implement Spring Security JWT auth with Compose Web login form"
"Set up Spring Boot WebSocket with real-time updates in Compose Web"
"Configure Spring Boot file upload with progress bar in Compose Web"
```

## Troubleshooting Common Issues

### Run Configuration Problems
- **Port Already in Use**: Check for running processes on ports 8080/8081
- **Gradle Daemon Issues**: Stop all daemons with `./gradlew --stop`
- **Class Not Found**: Rebuild project and refresh Gradle
- **Frontend Can't Connect**: Verify CORS configuration and backend URL

### Development Environment Issues
- **Hot Reload Not Working**: Check DevTools and Webpack configuration
- **Database Connection Failed**: Verify PostgreSQL is running
- **API 404 Errors**: Check controller mappings and context path
- **WebSocket Connection Refused**: Verify STOMP endpoint configuration

## Performance Optimization

### Backend Optimization
- Enable Spring Boot Actuator for monitoring
- Configure connection pooling (HikariCP)
- Implement caching with Spring Cache
- Use pagination for large datasets
- Enable GZIP compression

### Frontend Optimization
- Implement virtual scrolling for lists
- Use suspense for lazy loading
- Optimize bundle size with tree shaking
- Cache API responses appropriately
- Implement optimistic UI updates

## Monitoring & Logging

### Spring Boot Logging
```java
@Slf4j
@RestController
public class DataController {
    
    @GetMapping("/api/data")
    public ResponseEntity<List<DataDto>> getData() {
        log.debug("Fetching all data");
        try {
            List<DataDto> data = dataService.getAllData();
            log.info("Successfully fetched {} records", data.size());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error fetching data", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
```

### Frontend Error Handling
```kotlin
class ApiClient {
    suspend fun fetchData(): Result<List<DataDto>> {
        return try {
            Result.success(client.get("$baseUrl/api/data").body())
        } catch (e: Exception) {
            console.error("API call failed", e)
            Result.failure(e)
        }
    }
}
```

## Quick Reference Commands for Claude Code

When working with Claude Code on full-stack projects:

- "Create Spring Boot controller with validation and corresponding Compose Web form"
- "Set up IntelliJ compound run configuration for Spring Boot and Compose Web"
- "Implement Spring Data JPA repository with Compose Web CRUD interface"
- "Configure Spring Security OAuth2 with Compose Web authentication flow"
- "Create Spring Boot exception handler with Compose Web error boundary"
- "Set up Spring Boot metrics endpoint with Compose Web dashboard"

Always specify:
- Spring Boot version (3.x)
- Kotlin Compose Multiplatform version
- Whether using Java or Kotlin for backend
- Database type (PostgreSQL, MySQL, etc.)
- Required run configuration setup