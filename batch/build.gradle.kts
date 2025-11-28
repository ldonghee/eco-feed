plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("plugin.spring")
}

dependencies {
  // 모듈 의존성
  implementation(project(":core"))
  implementation(project(":infra"))

  // Kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  // Jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // Logging
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

  // Spring Boot
  implementation("org.springframework.boot:spring-boot-starter")

  // Jsoup (HTML 파싱)
  implementation("org.jsoup:jsoup:1.17.2")

  // OkHttp (HTTP 클라이언트)
  implementation("com.squareup.okhttp3:okhttp:4.12.0")

  // Redis
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
}