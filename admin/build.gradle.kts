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

  // Spring Boot MVC
  implementation("org.springframework.boot:spring-boot-starter-web")

  // Thymeleaf
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

  // Validation
  implementation("org.springframework.boot:spring-boot-starter-validation")

  // Redis
  implementation("org.springframework.boot:spring-boot-starter-data-redis")

  // OkHttp (Instagram API)
  implementation("com.squareup.okhttp3:okhttp:4.12.0")

  // Image Processing
  implementation("org.imgscalr:imgscalr-lib:4.2")

  // File Upload
  implementation("commons-io:commons-io:2.15.1")

  // Testing
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.mockito", module = "mockito-core")
  }
  testImplementation("com.ninja-squad:springmockk:4.0.2")
}