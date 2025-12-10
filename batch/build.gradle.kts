plugins {
  id("org.springframework.boot")
}

dependencies {
  // 모듈 의존성
  implementation(project(":core"))
  implementation(project(":infra"))

  // Spring Boot
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  // Scheduler
  implementation("org.springframework.boot:spring-boot-starter-quartz")

  // Web Scraping
  implementation("org.jsoup:jsoup:1.18.1")

  // HTTP Client
  implementation("io.projectreactor.netty:reactor-netty")

  // Message Queue (Redis Streams or RabbitMQ)
  implementation("org.springframework.boot:spring-boot-starter-amqp")
  // or
  // Redis Streams 는 이미 infra 에 포함

  // Test
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
  testImplementation("io.kotest:kotest-assertions-core:5.9.1")
  testImplementation("io.mockk:mockk:1.13.12")
}

tasks.bootJar {
  enabled = true
  archiveFileName.set("eco-feed-batch.jar")
}

tasks.jar {
  enabled = false
}