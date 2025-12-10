plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("plugin.spring")
}

dependencies {
  // 모듈 의존성
  implementation(project(":core"))
  implementation(project(":infra"))

  // Spring Boot Starters
  implementation("org.springframework.boot:spring-boot-starter-batch")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.bootJar {
  enabled = true
  archiveFileName.set("eco-feed-batch.jar")
}

tasks.jar {
  enabled = true
}