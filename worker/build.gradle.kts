plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("plugin.spring")
}

dependencies {
  // 모듈 의존성
  implementation(project(":core"))

  // Kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")

  // Jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // Logging
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

  // Spring WebFlux
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  // R2DBC
//  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//  implementation("io.asyncer:r2dbc-mysql:1.0.5")

  // Redis Reactive
  implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

  // Ktor Client (Claude API)
  val ktorVersion = "2.3.9"
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
}