plugins {
  id("org.springframework.boot") apply false
  id("io.spring.dependency-management")

  kotlin("plugin.spring")
  kotlin("plugin.jpa")
}

dependencies {
  // 모듈 의존성
  api(project(":core"))

  // Kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  // Jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

  // Spring Data JPA
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  // Spring Data Redis
  implementation("org.springframework.boot:spring-boot-starter-data-redis")

  // MySQL
  runtimeOnly("com.mysql:mysql-connector-j")

  // Flyway
  implementation("org.flywaydb:flyway-core")
  implementation("org.flywaydb:flyway-mysql")
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

dependencyManagement {
  imports {
    mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
  }
}