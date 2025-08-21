plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.gababartnicka.kotlinwebjourney"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_24
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.5")
    implementation("org.springframework.data:spring-data-jpa:3.5.3")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}